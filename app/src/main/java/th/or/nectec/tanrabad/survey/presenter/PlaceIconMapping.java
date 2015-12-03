package th.or.nectec.tanrabad.survey.presenter;

import java.util.HashMap;

import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.R;

public class PlaceIconMapping {
    private static PlaceIconMapping instances;
    HashMap<Integer, Integer> containerIconMapper = new HashMap<>();

    public PlaceIconMapping() {
        containerIconMapper.put(Place.TYPE_VILLAGE_COMMUNITY, R.mipmap.ic_logo_village);
        containerIconMapper.put(Place.SUBTYPE_TEMPLE, R.mipmap.ic_logo_temple);
        containerIconMapper.put(Place.SUBTYPE_CHURCH, R.mipmap.ic_logo_church);
        containerIconMapper.put(Place.SUBTYPE_MOSQUE, R.mipmap.ic_logo_mosque);
        containerIconMapper.put(Place.TYPE_SCHOOL, R.mipmap.ic_logo_school);
        containerIconMapper.put(Place.TYPE_HOSPITAL, R.mipmap.ic_logo_hospital);
        containerIconMapper.put(Place.TYPE_FACTORY, R.mipmap.ic_logo_factory);
    }

    public static int getPlaceIcon(Place place) {
        if (instances == null)
            instances = new PlaceIconMapping();
        if (place.getType() == Place.TYPE_WORSHIP) {
            return instances.containerIconMapper.get(place.getSubType());
        } else {
            return instances.containerIconMapper.get(place.getType());
        }
    }
}