import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

public class App {

    public static void main(String[] args) throws IOException {
//        LocalDateTime dateTime = LocalDateTime.of(2017, 1, 1, 1, 1, 1);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(2017,0,1);
//        calendar.set(Calendar.HOUR_OF_DAY,23);
//        calendar.set(Calendar.MINUTE,0);
//        calendar.set(Calendar.SECOND,0);
//
//        calendar.add(Calendar.DAY_OF_YEAR, 20);
//
//        System.out.println(dateFormat.format(calendar.getTime()));
//        calendar.clear();
//        System.out.println(dateFormat.format(calendar.getTime()));


        Generatable[] generatables = new Generatable[]{new CustomerGenerator(), new ItemGenerator(), new OrderGenerator()};


        for (Generatable generatable : generatables) {
            generatable.execute();
        }
    }
}
