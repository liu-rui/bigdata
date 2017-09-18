package com.github.liurui.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/***
 * 通过FileSystem方式调用
 */
public class Demo2 {
    public static void main(String[] args) throws IOException, URISyntaxException {
        FileSystem fileSystem;


        fileSystem = FileSystem.get(new Configuration());
        //file
        System.out.println(fileSystem.getScheme());

        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS" ,"hdfs://172.18.115.110:9000");
        fileSystem = FileSystem.get(configuration);
        //hdfs
        System.out.println(fileSystem.getScheme());
        System.out.println(fileSystem.getHomeDirectory());

        fileSystem = FileSystem.get(new URI("file:///"),configuration);
        //file，以url的scheme为主
        System.out.println(fileSystem.getScheme());
    }

}
