package th.or.nectec.tanrabad.survey.utils.time;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.TimeZone;

public class ThaiDateTimeConverter {

    public static final String ASIA_BANGKOK = "Asia/Bangkok";

    public static DateTime convert(String time) {
        return DateTime.parse(time).withZone(getThaiDateTimeZone());
    }

    private static DateTimeZone getThaiDateTimeZone() {
        return DateTimeZone.forTimeZone(TimeZone.getTimeZone(ASIA_BANGKOK));
    }
}
