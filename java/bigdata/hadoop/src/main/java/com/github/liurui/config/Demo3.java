package com.github.liurui.config;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.StringTokenizer;

/***
 * 使用计数器统计无效的分数（小于０或者大于１００）
 */
public class Demo3 {

    public static final Log  LOG = LogFactory.getLog(Demo3.class);

    enum ExceptionScore {
        A
    }


    public static class MyMapper extends Mapper<Object, Text, Text, IntWritable> {
        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer stringTokenizer = new StringTokenizer(value.toString());
            String name = stringTokenizer.nextToken();
            int score = Integer.parseInt(stringTokenizer.nextToken());

            if (score < 0 || score > 100) {
                context.getCounter(ExceptionScore.A).increment(1);
                LOG.error(String.format("error %s -> %d", name, score));
            } else
                context.write(new Text(name), new IntWritable(score));
        }
    }

    public static class MyReduce extends Reducer<Text, IntWritable, Text, IntWritable> {
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
        private Configuration _conf;

        public int run(String[] args) throws Exception {
            if(args.length !=2){
                System.err.println(String.format("%s input output" , getClass().getSimpleName()));
                ToolRunner.printGenericCommandUsage(System.err);
                return 1;
            }

            Job job = Job.getInstance(_conf);

            job.setJobName("test");
            job.setJarByClass(getClass());
            job.setMapperClass(MyMapper.class);
            job.setReducerClass(MyReduce.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);

            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            return job.waitForCompletion(true) ? 0 : 1;
        }

        public void setConf(Configuration conf) {
            _conf = conf;
        }

        public Configuration getConf() {
            return _conf;
        }
    }


    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new MyTool(), args));
    }


}
