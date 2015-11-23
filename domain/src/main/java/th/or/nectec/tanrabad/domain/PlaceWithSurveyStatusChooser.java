package th.or.nectec.tanrabad.domain;

import java.util.ArrayList;

import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.User;

public class PlaceWithSurveyStatusChooser {
    private UserRepository userRepository;
    private SurveyRepository surveyRepository;
    private PlaceWithSurveyStatusChooserPresenter placeWithSurveyStatusChooserPresenter;

    public PlaceWithSurveyStatusChooser(UserRepository userRepository,
                                        SurveyRepository surveyRepository,
                                        PlaceWithSurveyStatusChooserPresenter placeWithSurveyStatusChooserPresenter) {

        this.userRepository = userRepository;
        this.surveyRepository = surveyRepository;
        this.placeWithSurveyStatusChooserPresenter = placeWithSurveyStatusChooserPresenter;
    }

    public void showSurveyPlaceList(String username) {
        User user = userRepository.findUserByName(username);
        if(user==null){
            placeWithSurveyStatusChooserPresenter.alertUserNotFound();
            return;
        }

        ArrayList<Place> surveyPlaces = surveyRepository.findByUserIn7Days(user);

        if(surveyPlaces!=null){
            placeWithSurveyStatusChooserPresenter.displaySurveyPlaceList(surveyPlaces);
        }else{
            placeWithSurveyStatusChooserPresenter.displaySurveyPlacesNotfound();
        }
    }
}
