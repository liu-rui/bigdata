package io;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.junit.Test;

import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class Demo2Test {

    @Test
    public void test() throws URISyntaxException, IOException {
        Class keyClass = IntWritable.class;
        Class valueClass = Text.class;
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", "hdfs://172.18.115.110:9000");
        SequenceFile.Writer writer = SequenceFile.createWriter(configuration,
                SequenceFile.Writer.file(new Path("a.seq")),
                SequenceFile.Writer.keyClass(keyClass),
                SequenceFile.Writer.valueClass(valueClass));

        writer.append(new IntWritable(34), new Text("hello hadoop"));
        writer.append(new IntWritable(3100), new Text("你好 大数据"));
        writer.close();


        SequenceFile.Reader reader = new SequenceFile.Reader(configuration, SequenceFile.Reader.file(new Path("a.seq")));

        try {
            assertEquals(keyClass, reader.getKeyClass());
            assertEquals(valueClass, reader.getValueClass());

            IntWritable key = new IntWritable();
            Text value = new Text();


            assertTrue(reader.next(key, value));
            assertEquals(34 , key.get());
            assertEquals("hello hadoop" , value.toString());

            assertTrue(reader.next(key, value));
            assertEquals(3100 , key.get());
            assertEquals("你好 大数据" , value.toString());
        } finally {
            IOUtils.closeStream(reader);
        }
    }
}
