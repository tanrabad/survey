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

package th.or.nectec.tanrabad.survey.utils.alert;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;
import th.or.nectec.tanrabad.survey.utils.SnackToast;

class SnackToastAlertMessage implements AlertMessage {
    private Context context;

    public SnackToastAlertMessage(Context context) {
        this.context = context;
    }

    @Override
    public void show(String message) {
        SnackToast.make(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void show(@StringRes int messageId) {
        SnackToast.make(context, context.getString(messageId), Toast.LENGTH_LONG).show();
    }
}
