package mapreduce;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

public class Demo2 {
    public static class MyFileInputFormat<K, V> extends FileInputFormat<K, V> {

        @Override
        protected boolean isSplitable(JobContext context, Path filename) {
            return false;
        }

        public RecordReader<K, V> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
            MyRecordReader<K, V> reader = new MyRecordReader<K, V>();

            reader.initialize(split, context);
            return reader;
        }
    }

    public static class MyRecordReader<K, V> extends RecordReader<K, V> {
        private InputSplit split;
        private TaskAttemptContext context;
        private boolean hadData = true;

        public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {

            this.split = split;
            this.context = context;
        }

        public boolean nextKeyValue() throws IOException, InterruptedException {
            boolean ret = hadData;

            if (hadData){
                hadData = false;
            }
            return ret;
        }

        public K getCurrentKey() throws IOException, InterruptedException {
            return null;
        }

        public V getCurrentValue() throws IOException, InterruptedException {
            return null;
        }

        public float getProgress() throws IOException, InterruptedException {
            return 0;
        }

        public void close() throws IOException {

        }
    }

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
        System.exit(ToolRunner.run(new Demo2.MyTool(), args));
    }
}
