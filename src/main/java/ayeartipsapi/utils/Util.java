package ayeartipsapi.utils;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Util {
    public static String Date2YMD(long second) {
        Instant instant = Instant.ofEpochSecond(second);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDateTime.format(formatter);
    }
}
