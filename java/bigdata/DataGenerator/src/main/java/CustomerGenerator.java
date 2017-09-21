import java.io.*;
import java.util.Random;

public class CustomerGenerator implements Generatable {
    static final String FILE_NAME = "customer.csv";
    public static final int MAX = 100000; //10万条记录

    @Override
    public void execute() throws IOException {
        Random random = new Random(System.currentTimeMillis());

        try (FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME)) {
            try (BufferedOutputStream outputStream = new BufferedOutputStream(fileOutputStream)) {
                for (int i = 1; i <= MAX; i++) {
                    String text = String.format("%1$d,%1$s,%2$d%n", i, random.nextInt(2));
                    outputStream.write(text.getBytes());
                }
            }
        }
    }
}
