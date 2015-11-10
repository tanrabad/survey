package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Survey;

public interface SurveyPresenter {
    void loadSurveySuccess(Survey survey);

    void loadSurveyFail();
}
