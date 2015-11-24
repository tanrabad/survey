package th.or.nectec.tanrabad.domain;

import java.util.List;
import java.util.UUID;

import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.User;

public class SurveyBuildingHistoryController {
    private UserRepository userRepository;
    private PlaceRepository placeRepository;
    private SurveyRepository surveyRepository;
    private SurveyBuildingPresenter surveyBuildingPresenter;

    public SurveyBuildingHistoryController(UserRepository userRepository, PlaceRepository placeRepository, SurveyRepository surveyRepository, SurveyBuildingPresenter surveyBuildingPresenter) {
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
        this.surveyRepository = surveyRepository;
        this.surveyBuildingPresenter = surveyBuildingPresenter;
    }


    public void showSurveyBuildingOf(String placeUUID, String username) {
        User user = checkUserExist(username);
        if (user == null) return;

        Place place = checkPlaceExist(placeUUID);
        if (place == null) return;

        List<Survey> buildings = surveyRepository.findByPlaceAndUserIn7Days(place, user);
        if (buildings == null) {
            surveyBuildingPresenter.alertSurveyBuildingsNotFound();
            return;
        }

        surveyBuildingPresenter.displaySurveyBuildingList(buildings);
    }

    private User checkUserExist(String username) {
        User user = userRepository.findUserByName(username);
        if (user == null) {

            surveyBuildingPresenter.alertUserNotFound();
            return null;
        }
        return user;
    }

    private Place checkPlaceExist(String placeUUID) {
        Place place = placeRepository.findPlaceByPlaceUUID(UUID.fromString(placeUUID));
        if (place == null) {

            surveyBuildingPresenter.alertPlaceNotFound();
            return null;
        }
        return place;
    }

}
