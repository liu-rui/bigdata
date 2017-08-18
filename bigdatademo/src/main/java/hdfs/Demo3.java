package hdfs;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.net.URI;

public class Demo3 {
    public static void main(String[] args) throws IOException {
        FileSystem fileSystem = FileSystem.get(URI.create("hdfs://172.18.115.110:9000"), new Configuration());
        FSDataInputStream inputStream = fileSystem.open(new Path("/input/1.txt"));

        try {
            IOUtils.copyBytes(inputStream, System.out, 4096, false);
            System.out.println("--------第一次输出结束");
            System.out.println(inputStream.getPos());
            inputStream.seek(0);
            System.out.println("---------第二次输出");
            IOUtils.copyBytes(inputStream, System.out, 4096, false);
        } finally {
            IOUtils.closeStream(inputStream);
        }

        FSDataOutputStream outputStream = fileSystem.create(new Path("aa.txt"), true);

        try {
            outputStream.writeUTF("hello world\nhello hadoop\n中国");
        } finally {

            outputStream.close();
        }
    }
}
