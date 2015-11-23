package th.or.nectec.tanrabad.domain;

import java.util.ArrayList;

import th.or.nectec.tanrabad.entity.Place;

public interface PlaceWithSurveyStatusChooserPresenter {
    void displaySurveyPlaceList(ArrayList<Place> surveyPlace);

    void alertUserNotFound();

    void displaySurveyPlacesNotfound();
}
