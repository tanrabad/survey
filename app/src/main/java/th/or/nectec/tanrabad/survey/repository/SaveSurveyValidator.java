package th.or.nectec.tanrabad.survey.repository;

import android.content.Context;
import android.widget.Toast;

import th.or.nectec.tanrabad.domain.SurveyValidator;
import th.or.nectec.tanrabad.entity.Survey;

public class SaveSurveyValidator implements SurveyValidator {
    Context context;

    public SaveSurveyValidator(Context context) {
        this.context = context;
    }

    @Override
    public boolean validate(Survey survey) {
        if (survey.getUser() == null) {
            Toast.makeText(context, "ไม่พบข้อมูลผู้ใช้งาน", Toast.LENGTH_LONG).show();
            return false;
        }

        if (survey.getSurveyBuilding() == null) {
            Toast.makeText(context, "ไม่พบข้อมูลอาคาร", Toast.LENGTH_LONG).show();
            return false;
        }

        if (survey.getResidentCount() < 1) {
            Toast.makeText(context, "โปรดระบุจำนวนผู้อาศัย", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }
}
