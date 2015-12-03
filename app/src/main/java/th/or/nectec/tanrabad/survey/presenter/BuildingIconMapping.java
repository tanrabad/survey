package th.or.nectec.tanrabad.survey.presenter;

import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.R;

public class BuildingIconMapping {

    public static int getBuildingIcon(Place place) {
        if (place.getType() == Place.TYPE_VILLAGE_COMMUNITY) {
            return R.mipmap.ic_logo_home;
        } else {
            return R.mipmap.ic_logo_building;
        }
    }
}