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
import android.support.v7.app.AlertDialog;
import android.widget.TextView;
import th.or.nectec.tanrabad.survey.R;

class DialogAlertMessage implements AlertMessage {
    private Context context;

    public DialogAlertMessage(Context context) {
        this.context = context;
    }

    @Override
    public void show(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setView(R.layout.dialog_message_alert);
        alertDialog.setPositiveButton(R.string.got_it, null);
        AlertDialog dialog = alertDialog.show();
        TextView messageView = (TextView) dialog.findViewById(R.id.dialog_message);
        messageView.setText(message);
    }

    @Override
    public void show(@StringRes int messageId) {
        show(context.getResources().getString(messageId));
    }
}
