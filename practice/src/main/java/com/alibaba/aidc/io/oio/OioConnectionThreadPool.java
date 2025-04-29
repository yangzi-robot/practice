package com.alibaba.aidc.io.oio;

import com.alibaba.aidc.io.config.NioDemoConfig;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OioConnectionThreadPool implements Runnable{

    public static ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(NioDemoConfig.SOCKET_SERVER_PORT);
            while (!Thread.interrupted()) {
                Socket socket = serverSocket.accept();
                //为新的socket连接创建一个新线程
                SocketHandler handler = new SocketHandler(socket);
                //线程池提交一个任务
                executorService.submit(handler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        OioConnectionThreadPool pool = new OioConnectionThreadPool();
        pool.run();
    }
}
