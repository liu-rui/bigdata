package com.github.liurui.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.util.Arrays;
import java.util.Map;


/***
 * 1.Configuration默认会加载core-default.xml和core.site.xml文件（Configuration　static块）
 * export HADOOP_CLASSPATH="target/classes"
 * hadoop config.Demo1 | grep  "ftp.replication"
 * 默认是３
 * 替换ftp.replication　为34
 * hadoop config.Demo1 -Dftp.replication=34  | grep  "ftp.replication"
 */
public class Demo1 {

    public static class MyTool implements Tool {
        static {
            Configuration.addDefaultResource("yarn-default.xml");
            Configuration.addDefaultResource("yarn-site.xml");
        }


        private Configuration configuration;

        public int run(String[] args) throws Exception {
            System.out.println("输入的参数：");
            System.out.println(Arrays.toString(args));

            for (Map.Entry<String, String> stringEntry : configuration) {
                System.out.println(String.format("%s:%s" , stringEntry.getKey() , stringEntry.getValue()));
            }
            System.out.println(configuration.get("aaa"));
            return 0;
        }

        public void setConf(Configuration conf) {
            configuration = conf;
        }

        public Configuration getConf() {
            return configuration;
        }
    }


    public static void main(String[] args) throws Exception {
        ToolRunner.run(new MyTool(), args);
    }
}

