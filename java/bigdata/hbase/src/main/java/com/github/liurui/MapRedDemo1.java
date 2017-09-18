package com.github.liurui;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.StringUtils;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/***
 * 使用mapreduce操作hbase，实现wordcount
 * 数据源来自hbase
 * 结果存入hdfs
 * 执行方式　　jar hbase.MapRedDemo1  输出目录
 * 服务器上执行：hadoop  jar a.jar  -Dzookeeper.znode.parent=/hbase-unsecure -Dhbase.zookeeper.quorum=host17218115111,host17218115112,host17218115113 output
 */
public class MapRedDemo1 {
    private static String TABLE_NAME = "customer1";
    private static byte[] COLUMN_FAMILY = "info".getBytes();
    private static byte[] COLUMN = "content".getBytes();

    public static class MyMapper extends TableMapper<Text, IntWritable> {
        private static Text k = new Text();
        private static IntWritable v = new IntWritable(1);

        @Override
        protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
            String[] strings = StringUtils.split(new String(value.getValue(COLUMN_FAMILY, COLUMN)), ' ');

            for (String string : strings) {
                k.set(string);
                context.write(k, v);
            }
        }
    }

    public static class MyReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private static IntWritable v = new IntWritable();

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;

            for (IntWritable value : values) {
                sum += value.get();
            }
            v.set(sum);
            context.write(key, v);
        }
    }

    public static class MyTool implements Tool {
        private Configuration conf;

        @Override
        public int run(String[] args) throws Exception {
            Job job = Job.getInstance(getConf());

            job.setJarByClass(getClass());

            Scan scan = new Scan();
            scan.setCaching(500);
            scan.setCacheBlocks(false);

            TableMapReduceUtil.initTableMapperJob(TABLE_NAME,
                    scan,
                    MyMapper.class,
                    Text.class,
                    IntWritable.class,
                    job,
                    false);

            job.setReducerClass(MyReducer.class);
            FileSystem fileSystem = FileSystem.get(getConf());
            Path outputPath = new Path(args[0]);

            if (fileSystem.exists(outputPath))
                fileSystem.delete(outputPath, true);

            FileOutputFormat.setOutputPath(job, outputPath);
            return job.waitForCompletion(true) ? 0 : 1;
        }

        @Override
        public void setConf(Configuration conf) {
            this.conf = conf;
        }

        @Override
        public Configuration getConf() {
            return conf;
        }
    }


    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(HBaseConfiguration.create(), new MyTool(), args));
    }
}
