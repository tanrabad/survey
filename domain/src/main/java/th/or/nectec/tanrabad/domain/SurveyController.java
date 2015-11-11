package th.or.nectec.tanrabad.domain;

import java.util.UUID;

import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.User;

public class SurveyController {
    private SurveyRepository surveyRepository;
    private SurveyPresenter surveyPresenter;

    private BuildingRepository buildingRepository;
    private UserRepository userRepository;

    public SurveyController(SurveyRepository surveyRepository, BuildingRepository buildingRepository, UserRepository userRepository, SurveyPresenter surveyPresenter) {
        this.surveyRepository = surveyRepository;
        this.buildingRepository = buildingRepository;
        this.userRepository = userRepository;
        this.surveyPresenter = surveyPresenter;
    }

    public void checkThisBuildingAndUserCanSurvey(String buildingUUID, String username) {
        User user = checkUserExist(username);
        if (user == null) return;

        Building building = checkBuildingExist(buildingUUID);
        if (building == null) return;

        Survey survey = surveyRepository.findByBuildingAndUserIn7Day(building, user);
        if (survey == null) {
            surveyPresenter.onNewSurvey(building, user);
        } else {
            surveyPresenter.onEditSurvey(survey);
        }
    }

    private User checkUserExist(String username) {
        User user = userRepository.findUserByName(username);
        if (user == null) {
            surveyPresenter.alertUserNotFound();
            return null;
        }
        return user;
    }

    private Building checkBuildingExist(String buildingUUID) {
        Building building = buildingRepository.findBuildingByUUID(UUID.fromString(buildingUUID));
        if (building == null) {
            surveyPresenter.alertBuildingNotFound();
            return null;
        }
        return building;
    }
}
