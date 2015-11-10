package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.User;

class SurveyController {
    private SurveyRepository surveyRepository;
    private SurveyPresenter surveyPresenter;

    public SurveyController(SurveyRepository surveyRepository, SurveyPresenter surveyPresenter) {
        this.surveyRepository = surveyRepository;
        this.surveyPresenter = surveyPresenter;
    }

    public void findSurveyByBuildingAndUser(Building building, User user) {

        if (building == null) {
            surveyPresenter.alertBuildingNotFound();
            return;
        }

        if (user == null) {
            surveyPresenter.alertUserNotFound();
            return;
        }

        Survey survey = surveyRepository.findByBuildingAndUserIn7Day(building, user);
        if (survey == null) {
            surveyPresenter.onNewSurvey(building, user);
        } else {
            surveyPresenter.onEditSurvey(survey);
        }
    }
}
