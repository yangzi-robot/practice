package com.alibaba.aidc.io.oio;

import com.alibaba.aidc.io.config.NioDemoConfig;

import java.io.*;
import java.net.Socket;

public class SocketHandler implements Runnable {

    final Socket socket;

    public SocketHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        byte[] input = new byte[NioDemoConfig.SERVER_BUFFER_SIZE];
        try {

            //创建文件的写入流
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //用当前线程名称作为写入的文件名称
            String fileName = Thread.currentThread().getName();
            File file = new File("src/main/java/com/alibaba/aidc/io/oio/file/" + fileName + ".txt");

            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            String line;

            while ((line = br.readLine()) != null) {
                writer.newLine();
                writer.write(line);
                writer.flush();
            }

            //模拟更长的执行时间
            Thread.sleep(100);

            socket.close();
            System.out.println("finished socket, write in file， currentTime: : " + System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
