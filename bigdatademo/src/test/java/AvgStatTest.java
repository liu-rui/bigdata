import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class AvgStatTest {

    @Test
    public void  testMapper() throws IOException {
        new MapDriver<Object, Text, Text, IntWritable>()
                .withMapper(new AvgStat.MyMapper())
                .withInput(new LongWritable( 1), new Text(  "a 45"))
                .withOutput(new Text("a" ), new IntWritable(45))
                .runTest();
    }

    @Test
    public void testReduce() throws IOException {
new ReduceDriver<Text, IntWritable, Text, IntWritable>()
        .withReducer(new AvgStat.MyReduce())
        .withInput( new Text(  "a") , Arrays.asList(new IntWritable(34), new IntWritable(50)))
        .withOutput(new Text("a" ) , new IntWritable(42))
        .runTest();
    }
}