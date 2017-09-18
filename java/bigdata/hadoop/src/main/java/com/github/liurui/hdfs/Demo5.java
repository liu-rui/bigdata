package com.github.liurui.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;

public class Demo5 {

    public static void main(String[] args) throws IOException, InterruptedException {
        FileSystem fileSystem = FileSystem.get(URI.create("hdfs://172.18.115.110:9000"), new Configuration());

        fileSystem.deleteOnExit(new Path("1.txt"));
        Thread.sleep(1000 * 2);


        FSDataOutputStream outputStream = fileSystem.create(new Path("1.txt"), true);

        try {
            outputStream.writeUTF("hello world\nhello hadoop\n中国");
        } finally {
            outputStream.close();
        }
        System.out.println(fileSystem.getFileStatus(new Path("1.txt")).getLen());
    }
}
