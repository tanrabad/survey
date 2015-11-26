package th.or.nectec.tanrabad.domain.place;

import java.util.List;

public interface PlaceWithSurveyStatusListPresenter {
    void alertUserNotFound();

    void displayPlacesNotfound();

    void displayAllSurveyPlaceList(List<PlaceWithSurveyStatus> buildingsWithSurveyStatuses);
}
