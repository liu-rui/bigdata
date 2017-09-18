package com.github.liurui.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.StringUtils;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;


/***
 * 为用户推荐好友
 * 数据:input/fof.txt
 * 格式：用户　好友列表
 * 思想：
 * 　１．算出用户的二度和关联度（注意要去除一度的数据，即已经是好友的数据）
 *   2.按照关联度降序排列，最大的就是要推荐的好友
 */
public class FofDemo {
    public static class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        Text k = new Text();
        IntWritable v = new IntWritable();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] strings = StringUtils.split(value.toString(), ' ');

            for (int i = 0; i < strings.length; i++) {
                for (int j = 0; j < strings.length; j++) {
                    if (i == j) continue;
                    k.set(String.format("%s\t%s", strings[i], strings[j]));
                    v.set(i == 0 || j == 0 ? 0 : 1);
                    context.write(k, v);
                }
            }
        }
    }

    public static class MyReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        IntWritable v = new IntWritable();

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;


            for (IntWritable value : values) {
                if (value.get() == 0) return;

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
            job.setMapperClass(MyMapper.class);
            job.setReducerClass(MyReducer.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);

            FileInputFormat.addInputPath(job, new Path((args[0])));

            Path outputPath = new Path(args[1]);

            FileSystem fileSystem = FileSystem.get(getConf());

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
            return this.conf;
        }
    }

    public static class FriendKey implements WritableComparable<FriendKey> {
        private String userName;
        private int count;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public int compareTo(FriendKey o) {
            Integer ret = userName.compareTo(o.userName);

            if (ret != 0) return ret;
            return -Integer.compare(count, o.count);
        }

        @Override
        public void write(DataOutput out) throws IOException {
            out.writeUTF(userName);
            out.writeInt(count);
        }

        @Override
        public void readFields(DataInput in) throws IOException {
            userName = in.readUTF();
            count = in.readInt();
        }
    }

    public static class FriendValue implements WritableComparable<FriendValue> {
        private String friendName;
        private int count;

        public String getFriendName() {
            return friendName;
        }

        public void setFriendName(String friendName) {
            this.friendName = friendName;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public int compareTo(FriendValue o) {
            Integer ret = friendName.compareTo(o.friendName);

            if (ret != 0) return ret;
            return -Integer.compare(count, o.count);
        }

        @Override
        public void write(DataOutput out) throws IOException {
            out.writeUTF(friendName);
            out.writeInt(count);
        }

        @Override
        public void readFields(DataInput in) throws IOException {
            friendName = in.readUTF();
            count = in.readInt();
        }
    }

    public static class FriendMapper extends Mapper<LongWritable, Text, FriendKey, FriendValue> {
        FriendKey k = new FriendKey();
        FriendValue v = new FriendValue();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] strings = StringUtils.split(value.toString(), '\t');

            k.setUserName(strings[0]);
            k.setCount(Integer.parseInt(strings[2]));
            v.setFriendName(strings[1]);
            v.setCount(k.getCount());

            context.write(k, v);
        }
    }

    public static class FriendGroup extends WritableComparator {

        public FriendGroup() {
            super(FriendKey.class, true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            FriendKey first = (FriendKey) a;
            FriendKey second = (FriendKey) b;

            return first.getUserName().compareTo(second.getUserName());
        }
    }

    public static class FriendReducer extends Reducer<FriendKey, FriendValue, Text, NullWritable> {
        @Override
        protected void reduce(FriendKey key, Iterable<FriendValue> values, Context context) throws IOException, InterruptedException {
            Iterator<FriendValue> iterator = values.iterator();

            if (iterator.hasNext()) {
                FriendValue friendValue = iterator.next();
                context.write(new Text(String.format("%s %s %d", key.getUserName(), friendValue.friendName, friendValue.count)), NullWritable.get());
            }
        }
    }

    public static class FriendTool implements Tool {

        private Configuration conf;

        @Override
        public int run(String[] args) throws Exception {


            Job job = Job.getInstance(getConf());

            job.setJarByClass(getClass());
            job.setMapperClass(FriendMapper.class);
            job.setGroupingComparatorClass(FriendGroup.class);
            job.setReducerClass(FriendReducer.class);


            job.setMapOutputKeyClass(FriendKey.class);
            job.setMapOutputValueClass(FriendValue.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(NullWritable.class);

            FileInputFormat.addInputPath(job, new Path((args[0])));
            Path outputPath = new Path(args[1]);

            FileSystem fileSystem = FileSystem.get(getConf());

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
            return this.conf;
        }
    }


    public static class MainTool implements Tool {

        private Configuration conf;

        @Override
        public int run(String[] args) throws Exception {
            MyTool myTool = new MyTool();
            FriendTool friendTool = new FriendTool();

            myTool.setConf(conf);
            friendTool.setConf(conf);

            int ret = myTool.run(new String[]{args[0], "tmp1"});

            if(ret !=0) return  ret;
            return friendTool.run(new String[]{"tmp1/part*",args[1]});
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
        System.exit(ToolRunner.run(new MainTool(), args));
    }
}
