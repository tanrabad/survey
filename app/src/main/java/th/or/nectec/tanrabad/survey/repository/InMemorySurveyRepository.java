package th.or.nectec.tanrabad.survey.repository;

import java.util.ArrayList;

import th.or.nectec.tanrabad.domain.SurveyRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.User;

public class InMemorySurveyRepository implements SurveyRepository {

    private static InMemorySurveyRepository instance;
    ArrayList<Survey> surveys;

    private InMemorySurveyRepository() {
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

    @Override
    public ArrayList<Survey> findByPlaceAndUserIn7Days(Place place, User user) {
        ArrayList<Survey> surveyBuilding = new ArrayList<>();
        for (Survey eachSurvey : surveys) {
            if (eachSurvey.getSurveyBuilding().getPlace().equals(place) && eachSurvey.getUser().equals(user)) {
                surveyBuilding.add(eachSurvey);
            }
        }
        return surveyBuilding.isEmpty() ? null : surveyBuilding;
    }

    @Override
    public ArrayList<Place> findByUserIn7Days(User user) {
        ArrayList<Place> surveyPlaces = new ArrayList<>();
        for (Survey eachSurvey : surveys) {
            if (eachSurvey.getUser().equals(user)) {
                Building surveyBuilding = eachSurvey.getSurveyBuilding();
                if(!surveyPlaces.contains(surveyBuilding.getPlace())){
                    surveyPlaces.add(surveyBuilding.getPlace());
                }
            }
        }
        return surveyPlaces.isEmpty() ? null : surveyPlaces;
    }
}
