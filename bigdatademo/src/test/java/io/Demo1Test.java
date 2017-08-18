package io;


import org.apache.hadoop.io.IntWritable;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;


public class Demo1Test {

    @Test
    public void test() throws IOException {
        IntWritable a = new IntWritable(123);
        IntWritable b = new IntWritable(100);

        assertTrue(1 == a.compareTo(b));


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        a.write(dataOutputStream);
        dataOutputStream.flush();

        byte[] bytes = byteArrayOutputStream.toByteArray();

        assertTrue(bytes.length != 0);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        IntWritable c = new IntWritable();
        c.readFields(dataInputStream);

        assertEquals(a, c);
    }
}