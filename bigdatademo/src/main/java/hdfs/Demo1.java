package hdfs;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/***
 * 通过url方式读取文件内容
 */
public class Demo1 {
    static {
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
    }

    public static void main(String[] args) throws IOException {
        InputStream inputStream = new URL("hdfs://172.18.115.110:9000/input/1.txt").openStream();

        try {
            IOUtils.copyBytes(inputStream, System.out, 4096, false);
        } finally {
            IOUtils.closeStream(inputStream);
        }
    }
}
