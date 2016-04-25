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

package org.tanrabad.survey.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import org.tanrabad.survey.utils.prompt.AlertDialogPromptMessage;
import org.tanrabad.survey.utils.prompt.PromptMessage;
import org.tanrabad.survey.R;

public class GpsUtils {

    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Activity.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void showGpsSettingsDialog(final Context context) {
        PromptMessage promptMessage = new AlertDialogPromptMessage(context);
        promptMessage.setOnConfirm(context.getString(R.string.enable_gps), new PromptMessage.OnConfirmListener() {
            @Override
            public void onConfirm() {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        promptMessage.setOnCancel(context.getResources().getString(R.string.cancel), null);
        promptMessage.show(context.getString(R.string.gps_dialog_tilte),
                context.getString(R.string.gps_dialog_message));
    }
}
