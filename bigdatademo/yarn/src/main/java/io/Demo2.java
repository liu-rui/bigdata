package io;

import com.sun.jndi.toolkit.url.Uri;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/***
 * hadoop Demo2 hdfs://172.18.115.110:9000 org.apache.hadoop.io.compress.GzipCodec data.txt 大小再阿达发sd
 * hadoop 类名　hdfs地址　压缩算法　文件名　　内容
 * hadoop Demo2 hdfs://172.18.115.110:9000 org.apache.hadoop.io.compress.BZip2Codec data.txt fssf是否散发的s!!!
 */
public class Demo2 {
    public static void main(String[] args) throws ClassNotFoundException, URISyntaxException, IOException {
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(new URI(args[0]), configuration);
        CompressionCodecFactory factory = new CompressionCodecFactory(configuration);
        CompressionCodec compressionCodec = factory.getCodecByClassName(args[1]);

        Path compressionFilePath = new Path(args[2] + compressionCodec.getDefaultExtension());
        System.out.println(compressionFilePath.toString());
        FSDataOutputStream outputStream = fileSystem.create(compressionFilePath, true);
        CompressionOutputStream compressionOutputStream = compressionCodec.createOutputStream(outputStream);
        try {
            compressionOutputStream.write(args[3].getBytes());
            compressionOutputStream.finish();
        } finally {
            IOUtils.closeStream(compressionOutputStream);
            IOUtils.closeStream(outputStream);
        }

        //根据压缩文件生成未压缩文件
        CompressionCodec decompressionCodec = factory.getCodec(compressionFilePath);
        String decompressionFilePath = CompressionCodecFactory.removeSuffix(compressionFilePath.toString(), decompressionCodec.getDefaultExtension());

        FSDataInputStream dataInputStream = fileSystem.open(compressionFilePath);
        CompressionInputStream decompressionCodecInputStream = decompressionCodec.createInputStream(dataInputStream);
        FSDataOutputStream dataOutputStream = fileSystem.create(new Path(decompressionFilePath), true);

        try {
            IOUtils.copyBytes(decompressionCodecInputStream, dataOutputStream, 4096, false);
        } finally {
            IOUtils.closeStream(dataInputStream);
            IOUtils.closeStream(decompressionCodecInputStream);
            IOUtils.closeStream(dataOutputStream);
        }
    }
}
