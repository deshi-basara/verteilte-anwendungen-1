import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Helper class for returning the current time & date.
 */
public class Clock {

    private static SimpleDateFormat timeFormatter = new SimpleDateFormat("kk:mm:ss");
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
    private static Date d = new Date();

    public static String date() {
        d.setTime(System.currentTimeMillis());
        return dateFormatter.format(d);
    }

    public static String time() {
        d.setTime(System.currentTimeMillis());
        return timeFormatter.format(d);
    }
}
