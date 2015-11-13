package th.or.nectec.tanrabad.survey.repository;

import android.content.Context;
import android.widget.Toast;

import th.or.nectec.tanrabad.domain.SurveyValidator;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.survey.R;

public class SaveSurveyValidator implements SurveyValidator {
    Context context;

    public SaveSurveyValidator(Context context) {
        this.context = context;
    }

    @Override
    public boolean validate(Survey survey) {
        if (survey.getUser() == null) {
            Toast.makeText(context, R.string.user_not_found, Toast.LENGTH_LONG).show();
            return false;
        }

        if (survey.getSurveyBuilding() == null) {
            Toast.makeText(context, R.string.building_not_found, Toast.LENGTH_LONG).show();
            return false;
        }

        if (survey.getResidentCount() < 1) {
            Toast.makeText(context, R.string.please_enter_resident, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }
}
