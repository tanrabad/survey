/*
 * Copyright (c) 2015 NECTEC
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

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

public class ResourceUtils {

    private Context context;

    public ResourceUtils(Context context) {
        this.context = context;
    }

    public static ResourceUtils from(Context context) {
        return new ResourceUtils(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Drawable getDrawable(@DrawableRes int drawableId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return context.getResources().getDrawable(drawableId, null);
        else
            //noinspection deprecation
            return context.getResources().getDrawable(drawableId);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public int getColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return context.getResources().getColor(color, context.getTheme());
        else
            //noinspection deprecation
            return context.getResources().getColor(color);
    }
}
