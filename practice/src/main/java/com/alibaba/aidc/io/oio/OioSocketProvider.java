package com.alibaba.aidc.io.oio;

import com.alibaba.aidc.io.config.NioDemoConfig;

import java.net.Socket;

public class OioSocketProvider implements Runnable {

    private final String data;

    public OioSocketProvider(String data) {
        this.data = data;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("127.0.0.1", 18899);
            socket.getOutputStream().write(data.getBytes());
            System.out.println("finished send socket, write in file， currentTime: : " + System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //模拟发送100个请求
        for (int i = 0; i < 5; i++) {
            OioSocketProvider provider = new OioSocketProvider("测试发送" + i);
            provider.run();
        }
    }
}