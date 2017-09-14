package io;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.IOException;

/***
 * 使用压缩
 *
 * 使用方法：
 * １．使用mvn package,进行打包
 * ２． export HADOOP_CLASSPATH="/media/liurui/data/code/bigdata/bigdatademo/target/bigdatademo-1.0-SNAPSHOT.jar:$HADOOP_CLASSPATH"
 * 3. echo hello | hadoop Demo1 org.apache.hadoop.io.compress.GzipCodec | gunzip
 */
public class Demo1 {
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        Class<?> name = Class.forName(args[0]);
        CompressionCodec codec = (CompressionCodec) ReflectionUtils.newInstance(name, new Configuration());

        CompressionOutputStream outputStream = codec.createOutputStream(System.out);
        IOUtils.copyBytes(System.in, outputStream, 4096, false);
        outputStream.finish();
    }
}
