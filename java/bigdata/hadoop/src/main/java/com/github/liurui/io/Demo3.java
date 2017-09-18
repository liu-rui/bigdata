package com.github.liurui.io;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

import java.io.IOException;

public class Demo3 {

    public static void main(String[] args) throws IOException {
        Configuration conf = new Configuration();

        conf.set("fs.defaultFS"  , "hdfs://localhost/");

        try(  SequenceFile.Reader reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(new Path("/user/root/logs/log-.1505701138695")))){
            LongWritable key = new LongWritable();
            Text val = new Text();

            while (reader.next(key, val)) {
                System.out.printf("%d:%s%n", key.get(), val);
            }
        }
    }
}
