package th.or.nectec.tanrabad.survey.utils.time;


import android.util.SparseArray;
import org.joda.time.DateTime;

import java.util.Locale;

public class ThaiDatePrinter {

    public static String print(DateTime dateTime) {
        return String.format(Locale.US, "%d %s %04d",
                dateTime.getDayOfWeek(),
                ThaiMonthMapper.getMonth(dateTime.getMonthOfYear()),
                dateTime.getYear() + 543);
    }

    public static class ThaiMonthMapper {
        private static final SparseArray<String> monthNameMap = new SparseArray<>();

        static {
            monthNameMap.put(1, "ม.ค.");
            monthNameMap.put(2, "ก.พ.");
            monthNameMap.put(4, "เม.ย.");
            monthNameMap.put(3, "มี.ค.");
            monthNameMap.put(5, "พ.ค.");
            monthNameMap.put(6, "มิ.ย.");
            monthNameMap.put(7, "ก.ค.");
            monthNameMap.put(8, "ส.ค.");
            monthNameMap.put(9, "ก.ย.");
            monthNameMap.put(10, "ต.ค.");
            monthNameMap.put(11, "พ.ย.");
            monthNameMap.put(12, "ธ.ค.");
        }

        public static String getMonth(int month) {
            return monthNameMap.get(month);
        }
    }
}
