package com.github.liurui.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/***
 * 最小化的mapreduce
 */
public class Demo1 {

    public static class MyTool implements Tool {

        private Configuration conf;

        public int run(String[] args) throws Exception {
            Job job = Job.getInstance(getConf());

            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));
            return job.waitForCompletion(true) ? 0 : 1;
        }

        public void setConf(Configuration conf) {

            this.conf = conf;
        }

        public Configuration getConf() {
            return conf;
        }
    }


    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new Demo1.MyTool(), args));
    }
}
