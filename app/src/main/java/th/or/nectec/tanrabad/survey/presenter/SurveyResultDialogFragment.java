package th.or.nectec.tanrabad.survey.presenter;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.R;

public class SurveyResultDialogFragment extends DialogFragment {

    public static final String PLACE_ID = "place_id";
    public static final String FRAGMENT_TAG = "survey_result";

    public static SurveyResultDialogFragment newInstances(Place place) {
        SurveyResultDialogFragment fragment = new SurveyResultDialogFragment();
        Bundle args = new Bundle();
        args.putString(PLACE_ID, place.getId().toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_survey_result, container, false);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity(), R.style.Dialog).show();
    }
}
