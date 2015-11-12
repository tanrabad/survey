package th.or.nectec.tanrabad.survey.repository;

import java.util.ArrayList;

import th.or.nectec.tanrabad.domain.SurveyRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.User;

/**
 * Created by N. Choatravee on 12/11/2558.
 */
public class InMemorySurveyRepository implements SurveyRepository {

    private static InMemorySurveyRepository instance;
    ArrayList<Survey> surveys;

    public InMemorySurveyRepository() {
        this.surveys = new ArrayList<>();
    }

    public static InMemorySurveyRepository getInstance() {
        if (instance == null)
            instance = new InMemorySurveyRepository();
        return instance;
    }

    @Override
    public boolean save(Survey survey) {
        surveys.add(survey);
        return false;
    }

    @Override
    public Survey findByBuildingAndUserIn7Day(Building building, User user) {
        for (Survey eachSurvey : surveys) {
            if (eachSurvey.getSurveyBuilding().equals(building) && eachSurvey.getUser().equals(user)) {
                return eachSurvey;
            }
        }
        return null;
    }
}
