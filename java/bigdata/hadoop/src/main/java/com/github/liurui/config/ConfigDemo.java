package com.github.liurui.config;

import org.apache.hadoop.conf.Configuration;

import java.util.Map;

/***
 * 操作配置文件
 */
public class ConfigDemo {
    public static void main(String[] args) {
        Configuration conf = new Configuration();

        conf.addResource("config/1.xml");
        System.out.println(conf.get("dfs.name.dir"));
        System.out.println(conf.get("dfs.data.dir"));

        conf.addResource("config/2.xml");
        System.out.println(conf.get("dfs.name.dir"));
        //dfs.data.dir 已经被覆盖
        System.out.println(conf.get("dfs.data.dir"));

        for (Map.Entry<String, String> entry : conf) {
            System.out.println(entry);
        }
    }
}
