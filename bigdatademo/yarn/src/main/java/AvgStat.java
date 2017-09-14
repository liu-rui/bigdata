import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
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
 * 统计平均数
 *
 * export HADOOP_CLASSPATH="target/classes/"
 * 按照hadoop的部署方式，应用运行也是三种方式
 * １．local
 *　　hadoop AvgStat -fs file:/// -jt local input output
 * 2.伪分布式
 * 3.分布式
 *  hadoop AvgStat /input /output
 */
public class AvgStat {
    public static class MyMapper extends Mapper<Object, Text, Text, IntWritable> {
        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer stringTokenizer = new StringTokenizer(value.toString());
            context.write(new Text(stringTokenizer.nextToken()), new IntWritable(Integer.parseInt(stringTokenizer.nextToken())));
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
            Job job = Job.getInstance(_conf);

            job.setJobName("test");
            job.setJarByClass(AvgStat.class);
            job.setMapperClass(MyMapper.class);
//            job.setCombinerClass(MyReduce.class);
            job.setReducerClass(MyReduce.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);

            FileInputFormat.addInputPath(job, new Path(args[0]));
            Path outputPath = new Path(args[1]);
            FileSystem fileSystem = FileSystem.get(_conf);

            if (fileSystem.exists(outputPath)) fileSystem.delete(outputPath, true);

            FileOutputFormat.setOutputPath(job, outputPath);

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