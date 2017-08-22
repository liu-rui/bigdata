package io;


import config.Demo1;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

/***
 * 使用顺序文件(sequecefile)存储数据，然后通过mapreduce计算平均值然后存储为sequecefile文件．
 * hadoop  io.Demo4  input output
 */
public class Demo4 {

    public static class MyReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            int count = 0;

            for (IntWritable value : values) {
                sum += value.get();
                count++;
            }

            context.write(key, new IntWritable(sum / count));
        }
    }

    public static class MyTool implements Tool {
        private Configuration configuration;
        private Class keyClass = Text.class;
        private Class valueClass = IntWritable.class;
        private static final String inputFileName = "a.seq";


        public int run(String[] args) throws Exception {
            if (args.length != 2) {
                System.err.println("input 2 args:  input output");
                ToolRunner.printGenericCommandUsage(System.err);
                return 1;
            }
            genericData(args[0]);

            return stat(args[0], args[1]);

        }

        private int stat(String input, String output) throws IOException, ClassNotFoundException, InterruptedException {
            Job job = Job.getInstance(configuration);

            job.setJarByClass(Demo4.class);
            job.setJobName("test");
            job.setMapperClass(Mapper.class);
            job.setReducerClass(MyReducer.class);
            job.setOutputKeyClass(keyClass);
            job.setOutputValueClass(valueClass);
            job.setInputFormatClass(SequenceFileInputFormat.class);
            job.setOutputFormatClass(SequenceFileOutputFormat.class);


            FileInputFormat.addInputPath(job, new Path(input));
            FileOutputFormat.setOutputPath(job, new Path(output));

            return job.waitForCompletion(true) ? 0 : 1;
        }

        private void genericData(String dir) throws IOException {
            Random random = new Random();
            random.setSeed(System.currentTimeMillis());

            SequenceFile.Writer writer = SequenceFile.createWriter(configuration,
                    SequenceFile.Writer.file(new Path(dir, inputFileName)),
                    SequenceFile.Writer.keyClass(keyClass),
                    SequenceFile.Writer.valueClass(valueClass));
            try {
                for (int i = 0; i < 10; i++) {
                    writer.append(new Text(String.valueOf((char) ('A' + random.nextInt(5)))), new IntWritable(random.nextInt(101)));
                }
            } finally {
                IOUtils.closeStream(writer);
            }
        }

        public void setConf(Configuration conf) {
            this.configuration = conf;
        }

        public Configuration getConf() {
            return configuration;
        }
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new Demo4.MyTool(), args));
    }
}
