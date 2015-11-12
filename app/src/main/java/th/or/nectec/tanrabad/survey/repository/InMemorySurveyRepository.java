package th.or.nectec.tanrabad.survey.repository;

import java.util.ArrayList;

import th.or.nectec.tanrabad.domain.SurveyRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.User;

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
        if (surveys.contains(survey)) {
            surveys.set(surveys.indexOf(survey), survey);
        } else {
            surveys.add(survey);
        }

        return true;
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
