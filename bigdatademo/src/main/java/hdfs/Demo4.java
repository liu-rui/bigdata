package hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

public class Demo4 {
    public static void main(String[] args) throws IOException {
        FileSystem fileSystem = FileSystem.get(URI.create("hdfs://172.18.115.110:9000"), new Configuration());

        for (FileStatus fileStatus : fileSystem.globStatus(new Path("/*/*"), new PathFilter() {

            public boolean accept(Path path) {
                return path.toString().matches("txt$");
            }
        })) {
            System.out.println(fileStatus);
        }
    }
}
