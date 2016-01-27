package th.or.nectec.tanrabad.survey.repository;

import th.or.nectec.tanrabad.domain.survey.SurveyRepository;
import th.or.nectec.tanrabad.entity.*;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.repository.persistence.DbSurveyRepository;

import java.util.List;
import java.util.UUID;

public class BrokerSurveyRepository implements SurveyRepository {

    private static BrokerSurveyRepository instance;
    private SurveyRepository cache;
    private SurveyRepository persistent;

    public BrokerSurveyRepository(SurveyRepository cache, SurveyRepository persistent) {
        this.cache = cache;
        this.persistent = persistent;
    }

    public static BrokerSurveyRepository getInstance() {
        if (instance == null)
            instance = new BrokerSurveyRepository(InMemorySurveyRepository.getInstance(), new DbSurveyRepository(TanrabadApp.getInstance()));
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
    public List<SurveyDetail> findSurveyDetail(UUID surveyId, int containerLocationID) {
        return persistent.findSurveyDetail(surveyId, containerLocationID);
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
    public void updateOrInsert(List<Survey> surveys) {
        persistent.updateOrInsert(surveys);
        cache.updateOrInsert(surveys);
    }
}
