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

package org.tanrabad.survey.utils.android;

import android.content.Context;

public final class DpCalculator {

    private static DpCalculator instance;
    private Context context;

    private DpCalculator(Context context) {
        this.context = context;
    }

    public static DpCalculator from(Context context) {
        if (instance == null)
            instance = new DpCalculator(context);
        return instance;
    }

    public int toPx(int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public int toDp(int px) {
        return (int) (px / (context.getResources().getDisplayMetrics().densityDpi / 160f));
    }
}
