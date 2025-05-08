package com.alibaba.aidc.io.nio.socketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Objects;

public class SocketReceiveServer {

    public static final ByteBuffer buffer = ByteBuffer.allocate(1024);

    public static void main(String[] args) throws IOException {
        SocketReceiveServer socketReceiveServer = new SocketReceiveServer();
        socketReceiveServer.startServer();
    }

    public void startServer() throws IOException {
        Selector selector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();

        //设置为非阻塞的
        serverSocketChannel.configureBlocking(false);

        //端口绑定
        InetSocketAddress inetSocketAddress = new InetSocketAddress(18899);
        serverSocket.bind(inetSocketAddress);

        //注册server socket channel到selector上
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("serverSocketChannel start listening... ");

        //阻塞等待selector返回连接状态
        while (selector.select() > 0) {
            Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
            while(selectionKeyIterator.hasNext()) {


                SelectionKey key = selectionKeyIterator.next();

                if(key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();

                    //获得连接的socket channel
                    SocketChannel socketChannel = channel.accept();
                    if(Objects.isNull(socketChannel)) {
                        continue;
                    }
                    //设置连接的socket channel并注册到selector上
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("socketChannel 连接成功，地址：" + socketChannel.getRemoteAddress());
                } else if(key.isReadable()) {
                    this.processData(key);
                }
                selectionKeyIterator.remove();
            }
        }

    }

    //网络连接可读后，处理收到的data
    private void processData(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        try {
            //进入写模式
            buffer.clear();
            int length = 0;
            while (( length = socketChannel.read(buffer)) > 0) {
                //切换为读取模式
                buffer.flip();

                System.out.println("processData 收到数据：" + new String(buffer.array(), 0, length));
                buffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
