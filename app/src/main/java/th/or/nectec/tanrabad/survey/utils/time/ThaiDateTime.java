package th.or.nectec.tanrabad.survey.utils.time;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.TimeZone;

public class ThaiDateTime {
    public static DateTime parse(String time) {
        return DateTime.parse(time).withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone("Asia/Bangkok")));
    }
}
