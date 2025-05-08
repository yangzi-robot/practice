package com.alibaba.aidc.io.nio.socketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class SocketSendServer {

    public static final ByteBuffer buffer = ByteBuffer.allocate(1024);

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();

        startSendServer(selector);

        while (selector.select() > 0) {
            Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
            while (selectionKeyIterator.hasNext()) {
                SelectionKey selectionKey = selectionKeyIterator.next();
                if(selectionKey.isConnectable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    if(socketChannel.finishConnect()) {
                        socketChannel.register(selector, SelectionKey.OP_WRITE);
                    }
                } else if (selectionKey.isWritable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    buffer.clear();
                    String msg = "test send msg from " + socketChannel.getRemoteAddress();
                    buffer.put(msg.getBytes());

                    buffer.flip();
                    socketChannel.write(buffer);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }
                selectionKeyIterator.remove();
            }

        }
    }


    public static void startSendServer(Selector selector) throws IOException {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 18899));

        socketChannel.register(selector, SelectionKey.OP_CONNECT);
    }

}
