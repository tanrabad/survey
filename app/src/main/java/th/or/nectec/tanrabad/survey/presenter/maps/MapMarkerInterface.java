package th.or.nectec.tanrabad.survey.presenter.maps;

import th.or.nectec.tanrabad.entity.Location;

public interface MapMarkerInterface {

    Location getMarkedLocation();

    void setMarkedLocation(Location location);
}
