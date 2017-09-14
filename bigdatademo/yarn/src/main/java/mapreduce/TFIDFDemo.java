package mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.util.EnumCounters;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueLineRecordReader;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.StringUtils;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.*;
import java.net.URI;
import java.util.*;

/***
 * TFIDF 词频－反向文档频率
 */
public class TFIDFDemo {
    public static class CalcTF {
        public static enum Counter {
            Total,
        }


        public static class TFMapper extends Mapper<Text, Text, Text, Text> {
            private static Text v = new Text();

            @Override
            protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
                context.getCounter(Counter.Total).increment(1);
                Map<String, Integer> map = new HashMap<>();
                int sum = 0;
                IKSegmenter segmenter = new IKSegmenter(new StringReader(value.toString()), true);

                while (true) {
                    Lexeme lexeme = segmenter.next();
                    if (lexeme == null) break;
                    String term = lexeme.getLexemeText();

                    if (map.containsKey(term))
                        map.put(term, map.get(term) + 1);
                    else
                        map.put(term, 1);
                    sum++;
                }
                StringBuilder sb = new StringBuilder();

                float finalSum = sum;
                map.forEach((k, v) -> {
                    if (sb.length() != 0) sb.append(",");
                    sb.append(String.format("%s:%f", k, v / finalSum));
                });
                v.set(sb.toString());
                context.write(key, v);
            }
        }


        public static class TFTool   {

            public long run(Configuration conf) throws Exception {
                conf.set(KeyValueLineRecordReader.KEY_VALUE_SEPERATOR, " ");

                Job job = Job.getInstance( conf);
                job.setJarByClass(getClass());
                job.setMapperClass(TFMapper.class);
                job.setInputFormatClass(KeyValueTextInputFormat.class);
                job.setMapOutputKeyClass(Text.class);
                job.setMapOutputValueClass(Text.class);
                job.setOutputKeyClass(Text.class);
                job.setOutputValueClass(Text.class);

                FileInputFormat.addInputPath(job, new Path("input/blog.txt"));

                Path outputPath = new Path("output/tf");
                FileSystem fileSystem = FileSystem.get(conf);

                if (fileSystem.exists(outputPath)) fileSystem.delete(outputPath, true);
                FileOutputFormat.setOutputPath(job, outputPath);
                if (!job.waitForCompletion(true))
                    throw new IllegalAccessException();

                return job.getCounters().findCounter(Counter.Total).getValue();
            }
        }
    }

    public static class CalcIDF {
        public static class IDFMapper extends Mapper<Text, Text, Text, NullWritable> {
            private static Text k = new Text();

            @Override
            protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
                String[] strings = StringUtils.split(value.toString(), ',');

                for (String string : strings) {
                    String[] strings1 = StringUtils.split(string, ':');

                    k.set(strings1[0]);
                    context.write(k, NullWritable.get());
                }
            }
        }

        public static class IDFReducer extends Reducer<Text, NullWritable, Text, FloatWritable> {
            private static FloatWritable v = new FloatWritable();

            @Override
            protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
                long recordCount = context.getConfiguration().getLong("record_count", 0);
                int sum = 0;

                for (NullWritable ignored : values) {
                    sum++;
                }
                v.set((float) recordCount / sum);
                context.write(key, v);
            }
        }

        public static class IDFTool  {


            public int run(Configuration  conf) throws Exception {
                conf.set(KeyValueLineRecordReader.KEY_VALUE_SEPERATOR, "\t");
                Job job = Job.getInstance(conf);

                job.setJarByClass(getClass());
                job.setMapperClass(IDFMapper.class);
                job.setReducerClass(IDFReducer.class);
                job.setInputFormatClass(KeyValueTextInputFormat.class);
                job.setMapOutputKeyClass(Text.class);
                job.setMapOutputValueClass(NullWritable.class);
                job.setOutputKeyClass(Text.class);
                job.setOutputValueClass(FloatWritable.class);

                FileInputFormat.addInputPath(job, new Path("output/tf"));

                Path outputPath = new Path("output/idf");
                FileSystem fileSystem = FileSystem.get(conf);

                if (fileSystem.exists(outputPath)) fileSystem.delete(outputPath, true);
                FileOutputFormat.setOutputPath(job, outputPath);
                if (!job.waitForCompletion(true))
                    throw new IllegalAccessException();
                return 0;
            }
        }
    }

    public static class Calc {
        public static class Item implements Comparable<Item> {
            String term;
            float value;


            @Override
            public int compareTo(Item o) {
                return -Float.compare(value, o.value);
            }
        }

        public static class MainMapper extends Mapper<Text, Text, Text, NullWritable> {
            private static Text k = new Text();
            private Map<String, Float> mapIDF = new HashMap<>();

            @Override
            protected void setup(Context context) throws IOException, InterruptedException {
                super.setup(context);
                FileSystem fileSystem = FileSystem.get(context.getConfiguration());
                FSDataInputStream inputStream = fileSystem.open(new Path("output/idf/part-r-00000"));
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                while (true) {
                    String line = reader.readLine();

                    if (line == null) break;
                    String[] strings = StringUtils.split(line, '\t');
                    mapIDF.put(strings[0], Float.parseFloat(strings[1]));
                }
            }

            @Override
            protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
                String[] strings = StringUtils.split(value.toString(), ',');
                List<Item> items = new ArrayList<>();

                for (String string : strings) {
                    String[] strings1 = StringUtils.split(string, ':');

                    Item item = new Item();
                    item.term = strings1[0];
                    item.value = Float.parseFloat(strings1[1]) * mapIDF.get(item.term);
                    items.add(item);
                }
                Collections.sort(items);
                StringBuilder sb = new StringBuilder();


                items.forEach(w -> {
                    if (sb.length() != 0) sb.append(",");
                    sb.append(String.format("%s:%f", w.term, w.value));
                });
                sb.insert(0, '\t');
                sb.insert(0, key.toString());
                k.set(sb.toString());
                context.write(k, NullWritable.get());

            }
        }

        public static class CalcTool {
            public int run(Configuration conf) throws Exception {
                conf.set(KeyValueLineRecordReader.KEY_VALUE_SEPERATOR, "\t");
                Job job = Job.getInstance(conf);

                job.setJarByClass(getClass());
                job.setMapperClass(MainMapper.class);

                job.setInputFormatClass(KeyValueTextInputFormat.class);
                job.setMapOutputKeyClass(Text.class);
                job.setMapOutputValueClass(NullWritable.class);
                job.setOutputKeyClass(Text.class);
                job.setOutputValueClass(NullWritable.class);
                FileInputFormat.addInputPath(job, new Path("output/tf"));


                Path outputPath = new Path("output/tfidf");
                FileSystem fileSystem = FileSystem.get(conf);

                if (fileSystem.exists(outputPath)) fileSystem.delete(outputPath, true);
                FileOutputFormat.setOutputPath(job, outputPath);
                if (!job.waitForCompletion(true))
                    throw new IllegalAccessException();
                return 0;
            }
        }
    }


    public static class MainTool implements Tool {
        private Configuration conf;

        @Override
        public int run(String[] args) throws Exception {
            long totalCount = new CalcTF.TFTool().run(conf);
            conf.setLong("record_count", totalCount);
            new CalcIDF.IDFTool().run(conf);
            new Calc.CalcTool().run(conf);
            return 0;
        }

        @Override
        public void setConf(Configuration conf) {
            this.conf = conf;
        }

        @Override
        public Configuration getConf() {
            return this.conf;
        }
    }

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new Configuration(), new MainTool(), args);
    }
}
