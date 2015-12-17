package th.or.nectec.tanrabad.survey.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import th.or.nectec.tanrabad.survey.R;

public class PlaceSearchActivity extends TanrabadActivity {

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, PlaceSearchActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_search);
    }

}
