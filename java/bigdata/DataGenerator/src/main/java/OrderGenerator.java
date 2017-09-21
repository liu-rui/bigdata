import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class OrderGenerator implements Generatable {
    static final String FILE_NAME = "order.csv";
    static final int VOLUMN_PER_SECOND = 400; //每秒产生的订单数
    static final int[] ACTIVE_HOURS = new int[]{10, 11, 12, 13, 14, 15, 16, 17};
    static final int BEGIN_YEAR = 2017;
    static final int DAY = 30;


    @Override
    public void execute() throws IOException {
        Random random = new Random(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        int orderId = 0;

        try (FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME)) {
            try (BufferedOutputStream outputStream = new BufferedOutputStream(fileOutputStream ,1024 * 1024 )) {
                for (int day = 0; day < DAY; day++) {
                    calendar.clear();
                    calendar.set(BEGIN_YEAR, 0, 1, 0, 0, 0);
                    calendar.add(Calendar.DAY_OF_YEAR, day);
                    for (int hour = 0; hour < ACTIVE_HOURS.length; hour++) {
                        calendar.set(Calendar.HOUR_OF_DAY, ACTIVE_HOURS[hour]);
                        for (int minute = 0; minute <= 59; minute++) {
                            calendar.set(Calendar.MINUTE, minute);
                            for (int second = 0; second <= 59; second++) {
                                calendar.set(Calendar.SECOND, second);
                                for (int volumn = 1; volumn <= VOLUMN_PER_SECOND; volumn++) {
                                    String text = String.format("%1$d,%2$d,%3$d,%4$d,%5$tF %5$tT%n",
                                            ++orderId,
                                            random.nextInt(CustomerGenerator.MAX) + 1,
                                            random.nextInt(ItemGenerator.MAX) + 1,
                                            random.nextInt(50) + 50,
                                            calendar.getTime());

                                    outputStream.write(text.getBytes());
                                    if (orderId % 10000000 == 0)
                                        System.out.printf("已经生成订单数:%d%n", orderId);
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.printf("生成订单数为:%d", orderId);
    }
}
