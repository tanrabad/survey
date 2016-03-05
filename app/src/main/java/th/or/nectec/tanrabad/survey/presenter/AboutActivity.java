package th.or.nectec.tanrabad.survey.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import th.or.nectec.tanrabad.survey.R;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        findViewById(R.id.about_contributor).setOnClickListener(onClickListener);
        findViewById(R.id.about_license).setOnClickListener(onClickListener);
        findViewById(R.id.about_eula).setOnClickListener(onClickListener);
    }
}
