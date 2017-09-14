package io;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.Text;

import java.io.IOException;

/***
 * 使用mapfile,支持按照key查找
 * mapfile会在文件夹下创建index和data文件，格式都是sequecefile，通过以下命令查看内容
 * hdfs dfs -text cat input/a/*　　　
 */
public class Demo5 {
    private static final String DIR = "input/a";
    private static Configuration configuration = new Configuration();

    public static void main(String[] args) throws IOException {
        writeFile();
        readFIle();
    }

    private static void writeFile() throws IOException {
        MapFile.Writer writer = new MapFile.Writer(configuration,
                new Path(DIR),
                MapFile.Writer.keyClass(Text.class),
                MapFile.Writer.valueClass(IntWritable.class));

        Text key = new Text();
        IntWritable value = new IntWritable();


        try {
            key.set("b");
            value.set(34);
            writer.append(key, value);

            key.set("c");
            value.set(56);
            writer.append(key, value);
        } finally {
            IOUtils.closeStream(writer);
        }
    }

    private static void readFIle() throws IOException {
        MapFile.Reader reader = new MapFile.Reader(new Path(DIR), configuration);
        Text key = new Text();
        IntWritable value = new IntWritable();

        try {
            key.set("c");
            if (reader.get(key, value) != null)
                System.out.println(String.format("%s:%d", key, value.get()));

            key.set("b");
            if (reader.get(key, value) != null)
                System.out.println(String.format("%s:%d", key, value.get()));

        } finally {
            IOUtils.closeStream(reader);
        }
    }

}
