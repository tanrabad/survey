package org.tanrabad.survey.presenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import org.tanrabad.survey.R;

public class AboutActivity extends TanrabadActivity {

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.about_contributor:
                    openWeb(Uri.parse("https://github.com/tanrabad/survey/graphs/contributors"));
                    break;
                case R.id.about_license:
                    AboutUtils.showLicense(AboutActivity.this);
                    break;
                case R.id.about_privacy:
                    openWeb(Uri.parse("https://github.com/tanrabad/survey/blob/master/PRIVACY.md"));
                    break;
            }
        }
    };

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, AboutActivity.class);
        activity.startActivity(intent);
    }


    private void openWeb(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setSupportActionBar(findViewById(R.id.toolbar));
        findViewById(R.id.about_contributor).setOnClickListener(onClickListener);
        findViewById(R.id.about_license).setOnClickListener(onClickListener);
        findViewById(R.id.about_privacy).setOnClickListener(onClickListener);
    }
}
