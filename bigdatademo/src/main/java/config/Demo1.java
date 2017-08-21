package config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import javax.print.DocFlavor;
import java.util.Arrays;


/***
 * 1.Configuration默认会加载core-default.xml和core.site.xml文件（Configuration　static块）
 * export HADOOP_CLASSPATH="/media/liurui/data/code/bigdata/bigdatademo/target/bigdatademo-1.0-SNAPSHOT.jar:$HADOOP_CLASSPATH"
 * hadoop config.Demo1
 * 默认是３
 * 替换ftp.replication　为34
 * hadoop config.Demo1 -Dftp.replication=34
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

            System.out.println("-----------");
            System.out.println(configuration.get("ftp.replication"));
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

