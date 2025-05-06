package com.alibaba.aidc.io.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioChannelCopyFileTest {

    public static final String sourceFilePath = "src/main/java/com/alibaba/aidc/io/nio/file/FileChannelReadFile.txt";

    public static final String targetFilePath = "src/main/java/com/alibaba/aidc/io/nio/file/FileChannelWriteFile.txt";

    public static void main(String[] args) throws IOException {
        File sFile = new File(sourceFilePath);
        File tFile = new File(targetFilePath);
        FileInputStream fis = null;
        FileOutputStream fos = null;

        FileChannel fisChannel = null;
        FileChannel fosChannel = null;
        try {
            //文件创建校验
            if(tFile.exists()) {
                tFile.delete();
            }
            tFile.createNewFile();

            //注意，java中的input stream和output stream都是以内存为操作对象的
            //input stream指创建流向内存写入数据，对应文件操作则为read
            //output stream指创建流从内存获取数据，对应文件操作则为write
            fis = new FileInputStream(sFile);
            fos = new FileOutputStream(tFile);

            fisChannel = fis.getChannel();
            fosChannel = fos.getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(1);
            int index = 1;
            while (fisChannel.read(buffer) != -1) {
                buffer.flip();

                while (fosChannel.write(buffer) != 0) {
                    System.out.printf("第%s写入%n", index);
                    index++;
                }

                //翻转
                buffer.clear();
            }
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            fisChannel.close();
            fosChannel.close();
            fis.close();
            fos.close();

        }

    }

}
