/*
 * Copyright (c) 2016 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tanrabad.survey.utils.time;


import android.util.SparseArray;
import org.joda.time.DateTime;

import java.util.Locale;

public class ThaiDatePrinter {

    public static String print(DateTime dateTime) {
        return String.format(Locale.US, "%d %s %04d",
                dateTime.getDayOfMonth(),
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
