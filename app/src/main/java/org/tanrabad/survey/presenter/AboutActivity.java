package org.tanrabad.survey.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import org.tanrabad.survey.R;

public class AboutActivity extends TanrabadActivity {

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.about_contributor:
                    break;
                case R.id.about_license:
                    AboutUtils.showLicense(AboutActivity.this);
                    break;
                case R.id.about_eula:
                    break;
            }
        }
    };

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, AboutActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        findViewById(R.id.about_contributor).setOnClickListener(onClickListener);
        findViewById(R.id.about_license).setOnClickListener(onClickListener);
        findViewById(R.id.about_eula).setOnClickListener(onClickListener);
    }
}
