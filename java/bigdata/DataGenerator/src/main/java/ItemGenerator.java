import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class ItemGenerator implements Generatable {
    static final String FILE_NAME = "item.csv";
    public static final int MAX = 10000; //1万条记录


    @Override
    public void execute() throws IOException {
        Random random = new Random(System.currentTimeMillis());

        try (FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME)) {
            try (BufferedOutputStream outputStream = new BufferedOutputStream(fileOutputStream)) {
                for (int i = 1; i <= MAX; i++) {
                    String text = String.format("%1$d,%1$s%n", i);
                    outputStream.write(text.getBytes());
                }
            }
        }
    }
}
