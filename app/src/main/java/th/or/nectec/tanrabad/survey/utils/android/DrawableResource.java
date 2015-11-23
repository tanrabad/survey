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
import android.support.annotation.DrawableRes;
import th.or.nectec.tanrabad.survey.TanrabadApp;

public class DrawableResource {

    private final int drawableId;
    private Context context;


    public DrawableResource(@DrawableRes int drawableId) {
        this.drawableId = drawableId;
        this.context = TanrabadApp.instance();
    }

    public static Drawable get(@DrawableRes int drawableId) {
        return new DrawableResource(drawableId).get();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Drawable get() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return context.getResources().getDrawable(drawableId, null);
        else
            //noinspection deprecation
            return context.getResources().getDrawable(drawableId);
    }
}
