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
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;
import org.apache.hadoop.util.StringUtils;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;


/***
 * 算出每个月中最大温度是哪天和温度
 * 数据：input/Temperature.text
 * 格式为:时间　温度
 * 实现思想：
 *  排序：按照年月升序排序然后按照温度降序排列
 *  分组：使用分组将年月相同的记录分到一个key中
 *
 */
public class TemperatureDemo {
    public static class Weather implements WritableComparable<Weather> {
        int year;
        int month;
        int wd;

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getWd() {
            return wd;
        }

        public void setWd(int wd) {
            this.wd = wd;
        }

        @Override
        public int compareTo(Weather o) {
            int ret = Integer.compare(year, o.year);

            if (ret != 0) return ret;
            ret = Integer.compare(month, o.month);

            if (ret != 0) return ret;
            return -Integer.compare(wd, o.wd);
        }

        @Override
        public void write(DataOutput out) throws IOException {
            out.writeInt(year);
            out.writeInt(month);
            out.writeInt(wd);
        }

        @Override
        public void readFields(DataInput in) throws IOException {
            year = in.readInt();
            month = in.readInt();
            wd = in.readInt();
        }

        @Override
        public String toString() {
            return "Weather{" +
                    "year=" + year +
                    ", month=" + month +
                    ", wd=" + wd +
                    '}';
        }
    }

    public  static class  WeatherValue implements WritableComparable<WeatherValue> {
        private String date;
        private  int wd;

        public WeatherValue() {
        }

        public WeatherValue(String date, int wd) {
            this.date = date;
            this.wd = wd;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getWd() {
            return wd;
        }

        public void setWd(int wd) {
            this.wd = wd;
        }

        @Override
        public int compareTo(WeatherValue o) {
            return Integer.compare(wd , o.wd);
        }

        @Override
        public void write(DataOutput out) throws IOException {
            out.writeUTF(date);
            out.writeInt(wd);
        }

        @Override
        public void readFields(DataInput in) throws IOException {
            date = in.readUTF();
            wd = in.readInt();
        }

        @Override
        public String toString() {
            return "WeatherValue{" +
                    "date='" + date + '\'' +
                    ", wd=" + wd +
                    '}';
        }
    }

    public static class MyMapper extends Mapper<LongWritable, Text, Weather, WeatherValue> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] strings = StringUtils.split(value.toString(), ',');
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateFormat.parse(strings[0]));
                Weather weather = new Weather();

                weather.setYear(calendar.get(Calendar.YEAR));
                weather.setMonth(calendar.get(Calendar.MONTH));
                weather.setWd(Integer.parseInt(strings[1].substring(0, strings[1].length() - 1)));

                System.out.println(String.format("%s %s", value, weather));
                context.write(weather, new WeatherValue(strings[0] , weather.getWd()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static class MyPartitioner extends HashPartitioner<Weather, WeatherValue> {

        @Override
        public int getPartition(Weather key, WeatherValue value, int numReduceTasks) {
            System.out.println("partition");
            return 0;
        }
    }

    public static class MySorter extends WritableComparator {

        public MySorter() {
            super(Weather.class, true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            Weather first = (Weather) a;
            Weather second = (Weather) b;
            System.out.println("sort");
            return first.compareTo(second);
        }
    }

    public static class MyGrouper extends WritableComparator {

        public MyGrouper() {
            super(Weather.class, true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            System.out.println("group");
            Weather first = (Weather) a;
            Weather second = (Weather) b;

            int ret = Integer.compare(first.year, second.year);

            if (ret != 0) return ret;
            return Integer.compare(first.month, second.month);
        }
    }


    public static class MyReducer extends Reducer<Weather, WeatherValue, Text, NullWritable> {

        @Override
        protected void reduce(Weather key, Iterable<WeatherValue> values, Context context) throws IOException, InterruptedException {
            Iterator<WeatherValue> iterator = values.iterator();

            if (iterator.hasNext()) {
                context.write(new Text( iterator.next().toString() ), NullWritable.get());
            }
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
            job.setPartitionerClass(MyPartitioner.class);
            job.setSortComparatorClass(MySorter.class);
            job.setGroupingComparatorClass(MyGrouper.class);

            job.setMapOutputKeyClass(Weather.class);
            job.setMapOutputValueClass(WeatherValue.class);

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


    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new MyTool(), args));
    }
}
