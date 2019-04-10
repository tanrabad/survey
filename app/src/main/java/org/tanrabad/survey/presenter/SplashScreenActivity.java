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

package org.tanrabad.survey.presenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import org.tanrabad.survey.R;
import org.tanrabad.survey.presenter.CheckVersionThread.CheckVersionListener;

public class SplashScreenActivity extends TanrabadActivity {

    private CheckVersionThread checker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ("android.intent.action.MAIN".equals(getIntent().getAction())) {
            checker = new CheckVersionThread(new CheckVersionListener() {
                @Override
                public void onAlreadyLatest() {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }

                @Override
                public void onFoundNewer(Version version) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashScreenActivity.this);
                    alertDialog.setView(R.layout.dialog_message_alert);
                    alertDialog.setPositiveButton(R.string.update, (dialog, which) -> {
                        Intent store = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=org.tanrabad.survey"));
                        startActivity(store);
                        finish();
                    });
                    alertDialog.setNegativeButton(R.string.close, (dialog, which) -> {
                        finish();
                    });
                    alertDialog.setCancelable(false);
                    AlertDialog dialog = alertDialog.show();
                    String msg = getString(R.string.found_newer_version, version);
                    ((TextView) dialog.findViewById(R.id.dialog_message)).setText(msg);
                }
            });
            checker.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        checker.pause();
    }

    @Override
    public void onBackPressed() {
        //Do nothing
    }
}
