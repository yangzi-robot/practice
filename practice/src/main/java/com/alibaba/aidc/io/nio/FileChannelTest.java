package com.alibaba.aidc.io.nio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {

    public static void main(String[] args) {
        try {
            RandomAccessFile descFile = new RandomAccessFile("src/main/java/com/alibaba/aidc/io/nio/file/FileChannelReadFile.txt", "rw");
            FileChannel fosChannel = descFile.getChannel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(1);
            while ( fosChannel.read(byteBuffer) != -1 ) {

                //翻转为读取模式
                byteBuffer.flip();

                //获取读取的内容
                byte[] bytes = new byte[byteBuffer.limit()];
                for (int i = 0; i < byteBuffer.limit(); i++)
                {
                    bytes[i] = byteBuffer.get();
                }

                //转换为string
                String value = new String(bytes);
                System.out.println(value);

                //转回写入模式
                byteBuffer.clear();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
