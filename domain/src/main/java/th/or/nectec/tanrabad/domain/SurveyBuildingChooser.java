package th.or.nectec.tanrabad.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.User;

public class SurveyBuildingChooser {

    private UserRepository userRepository;
    private PlaceRepository placeRepository;
    private BuildingRepository buildingRepository;
    private SurveyRepository surveyRepository;
    private SurveyBuildingListPresenter surveyBuildingPresenter;

    public SurveyBuildingChooser(UserRepository userRepository, PlaceRepository placeRepository, BuildingRepository buildingRepository, SurveyRepository surveyRepository, SurveyBuildingListPresenter surveyBuildingPresenter) {

        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
        this.buildingRepository = buildingRepository;
        this.surveyRepository = surveyRepository;
        this.surveyBuildingPresenter = surveyBuildingPresenter;
    }

    public void displaySurveyBuildingOf(String placeUUID, String username) {
        User user = userRepository.findUserByName(username);
        if (user == null) {
            surveyBuildingPresenter.alertUserNotFound();
            return;
        }

        Place place = placeRepository.findPlaceByPlaceUUID(UUID.fromString(placeUUID));
        if (place == null) {
            surveyBuildingPresenter.alertPlaceNotFound();
            return;
        }

        List<Building> buildings = buildingRepository.findBuildingInPlace(place.getId());

        if (buildings == null) {
            surveyBuildingPresenter.alertBuildingsNotFound();
            return;
        }

        List<Building> surveyBuildings = surveyRepository.findByPlaceAndUserIn7Days(place, user);
        List<SurveyBuilding> surveyBuildingsWithStatus = new ArrayList<>();
        for (Building eachBuilding : buildings) {
            SurveyBuilding surveyBuildingStatus = new SurveyBuilding(eachBuilding, surveyBuildings != null && surveyBuildings.remove(eachBuilding));
            surveyBuildingsWithStatus.add(surveyBuildingStatus);
        }

        surveyBuildingPresenter.displayAllSurveyBuildingList(surveyBuildingsWithStatus);
    }
}
