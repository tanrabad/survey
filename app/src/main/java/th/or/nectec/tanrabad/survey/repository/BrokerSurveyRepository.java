package th.or.nectec.tanrabad.survey.repository;

import java.util.List;
import java.util.UUID;

import th.or.nectec.tanrabad.domain.building.BuildingWithSurveyStatus;
import th.or.nectec.tanrabad.domain.survey.SurveyRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.SurveyDetail;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.repository.persistence.DbSurveyRepository;

public class BrokerSurveyRepository implements SurveyRepository {

    private static BrokerSurveyRepository instance;
    private final SurveyRepository cache;
    private final SurveyRepository persistent;

    private BrokerSurveyRepository(SurveyRepository cache, SurveyRepository persistent) {
        this.cache = cache;
        this.persistent = persistent;
    }

    public static BrokerSurveyRepository getInstance() {
        if (instance == null)
            instance = new BrokerSurveyRepository(InMemorySurveyRepository.getInstance(),
                    new DbSurveyRepository(TanrabadApp.getInstance()));
        return instance;
    }

    @Override
    public Survey findByBuildingAndUserIn7Day(Building building, User user) {
        return persistent.findByBuildingAndUserIn7Day(building, user);
    }

    @Override
    public List<Survey> findByPlaceAndUserIn7Days(Place place, User user) {
        return persistent.findByPlaceAndUserIn7Days(place, user);
    }

    @Override
    public List<BuildingWithSurveyStatus> findSurveyBuilding(Place place, User user) {
        return persistent.findSurveyBuilding(place, user);
    }

    @Override
    public List<BuildingWithSurveyStatus> findSurveyBuildingByBuildingName(
            Place place, User user, String buildingName) {
        return persistent.findSurveyBuildingByBuildingName(place, user, buildingName);
    }

    @Override
    public List<SurveyDetail> findSurveyDetail(UUID surveyId, int containerLocationId) {
        return persistent.findSurveyDetail(surveyId, containerLocationId);
    }

    @Override
    public List<Place> findByUserIn7Days(User user) {
        return persistent.findByUserIn7Days(user);
    }

    @Override
    public boolean save(Survey survey) {
        boolean success = persistent.save(survey);
        if (success)
            cache.save(survey);
        return success;
    }

    @Override
    public boolean update(Survey survey) {
        boolean success = persistent.update(survey);
        if (success)
            cache.update(survey);
        return success;
    }

    @Override
    public boolean delete(Survey survey) {
        boolean success = persistent.delete(survey);
        if (success) {
            cache.delete(survey);
        }
        return success;
    }

    @Override
    public void updateOrInsert(List<Survey> surveys) {
        persistent.updateOrInsert(surveys);
        cache.updateOrInsert(surveys);
    }
}
