package mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.StringUtils;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.Arrays;

/***
 * pagerank
 *
 */
public class PageRankDemo {
    public static class Node {
        private String first;
        private double weight;
        private String[] others;

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public String[] getOthers() {
            return others;
        }

        public void setOthers(String[] others) {
            this.others = others;
        }

        public boolean ExistsOthers() {
            return others != null && others.length != 0;
        }

        public static Node parse(String txt, boolean isFirst) {
            Node ret = new Node();
            String[] strings = StringUtils.split(txt.toString(), ' ');

            ret.first = strings[0];

            if (isFirst) {
                ret.weight = 1.0;

                if (strings.length > 1)
                    ret.others = Arrays.copyOfRange(strings, 1, strings.length);
            } else {
                ret.weight = Double.parseDouble(strings[1]);

                if (strings.length > 2)
                    ret.others = Arrays.copyOfRange(strings, 2, strings.length);
            }
            return ret;
        }


        @Override
        public String toString() {
            return String.format("%s %s %s", first, weight, String.join(" ", others));
        }
    }


    public static class MyMapper extends Mapper<LongWritable, Text, Text, Text> {
        private static Text k = new Text();
        private static Text v = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            Node node = Node.parse(value.toString(), context.getConfiguration().getInt("step", 1) == 1);
            double weight = node.getWeight() / node.getOthers().length;

            for (int i = 0; i < node.getOthers().length; i++) {
                k.set(node.getOthers()[i]);
                v.set(Double.toString(weight));
                context.write(k, v);
            }
            k.set(node.first);
            v.set(node.toString());
            context.write(k, v);
        }
    }


    public static class MyReducer extends Reducer<Text, Text, Text, NullWritable> {

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            double sum = 0;
            Node node = null;

            for (Text value : values) {
                if (value.toString().contains(" ")) {
                    node = Node.parse(value.toString(), false);
                } else {
                    double weight = Double.parseDouble(value.toString());
                    sum += weight;
                }
            }
            context.getCounter(A.BB).increment((long) (1000 * Math.abs(sum - node.getWeight())));

            node.setWeight(sum);
            context.write(new Text(node.toString()), NullWritable.get());
        }
    }

    public enum A {
        BB
    }


    public static class MyTool implements Tool {

        private Configuration conf;

        public int run(String[] args) throws Exception {
            int ret = 0;
            int i = 0;

            while (true) {
                i++;
                conf.setInt("step", i);
                ret = execute(i == 1 ? args[0] : String.format("%s/step%d", args[1], i - 1), String.format("%s/step%d", args[1], i));
                if (ret != 0) break;
                if (i == 2) break;
            }
            return ret;
        }

        private int execute(String input, String output) throws IOException, ClassNotFoundException, InterruptedException {
            Job job = Job.getInstance(getConf());

            job.setMapperClass(MyMapper.class);
            job.setReducerClass(MyReducer.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);
            FileInputFormat.addInputPath(job, new Path(input));

            Path outputPath = new Path(output);
            FileSystem fileSystem = FileSystem.get(getConf());

            if (fileSystem.exists(outputPath))
                fileSystem.delete(outputPath, true);

            FileOutputFormat.setOutputPath(job, outputPath);
            int ret = job.waitForCompletion(true) ? 0 : 1;


            System.out.println(job.getCounters().findCounter(A.BB).getValue());
            return ret;
        }


        public void setConf(Configuration conf) {

            this.conf = conf;
        }

        public Configuration getConf() {
            return conf;
        }
    }


    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new MyTool(), args));
    }
}
