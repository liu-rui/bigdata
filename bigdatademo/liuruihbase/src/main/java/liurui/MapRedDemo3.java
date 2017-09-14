package liurui;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.StringUtils;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import java.io.IOException;

/***
 * 使用mapreduce操作hbase，实现wordcount
 * 数据源来自hbase
 * 结果存入hbase
 * 执行方式　　jar hbase.MapRedDemo3
 */
public class MapRedDemo3 {
    private static String DIST_TABLE_NAME = "wordcount";
    private static byte[] DIST_COLUMN_FAMILY = "info".getBytes();
    private static byte[] DIST_COLUMN = "count".getBytes();
    private static String TABLE_NAME = "customer1";
    private static byte[] COLUMN_FAMILY = "info".getBytes();
    private static byte[] COLUMN = "content".getBytes();

    public static class MyMapper extends TableMapper<Text, IntWritable> {
        private static Text k = new Text();
        private static IntWritable v = new IntWritable(1);

        @Override
        protected void map(ImmutableBytesWritable key, Result value, Mapper.Context context) throws IOException, InterruptedException {
            String[] strings = StringUtils.split(new String(value.getValue(COLUMN_FAMILY, COLUMN)), ' ');

            for (String string : strings) {
                k.set(string);
                context.write(k, v);
            }
        }
    }

    public static class MyReducer extends TableReducer<Text, IntWritable, ImmutableBytesWritable> {
        private static IntWritable v = new IntWritable();

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;

            for (IntWritable value : values) {
                sum += value.get();
            }
            v.set(sum);

            Put put = new Put(key.toString().getBytes());
            put.addColumn(DIST_COLUMN_FAMILY, DIST_COLUMN, String.valueOf(sum).getBytes());
            context.write(null, put);
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
                    MapRedDemo1.MyMapper.class,
                    Text.class,
                    IntWritable.class,
                    job,
                    false);
            TableMapReduceUtil.initTableReducerJob(DIST_TABLE_NAME, MyReducer.class, job);
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
        System.exit(ToolRunner.run(new MyTool(), args));
    }
}
