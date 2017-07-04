package org.tanrabad.survey.presenter;

import android.app.Activity;
import android.support.v4.app.Fragment;

public class TanrabadFragment extends Fragment {

    protected void runOnUiThread(Runnable action) {
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(action);
        }
    }
}
