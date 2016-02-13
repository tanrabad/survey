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

package th.or.nectec.tanrabad.survey.utils.android;

import android.content.Context;

public class DPCalculator {

    private static DPCalculator instance;
    private int dp;
    private Context context;

    public DPCalculator(Context context) {
        this.context = context;
    }

    public static DPCalculator from(Context context) {
        if (instance == null)
            instance = new DPCalculator(context);
        return instance;
    }

    public int toPX(int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public int toDP(int px) {
        return (int) (px / (context.getResources().getDisplayMetrics().densityDpi / 160f));
    }
}
