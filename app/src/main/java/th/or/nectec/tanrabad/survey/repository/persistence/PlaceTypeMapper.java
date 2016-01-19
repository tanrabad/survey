package th.or.nectec.tanrabad.survey.repository.persistence;

import android.util.SparseIntArray;
import th.or.nectec.tanrabad.entity.Place;

public class PlaceTypeMapper {
    private static PlaceTypeMapper instances;
    private SparseIntArray placeSubTypeMapping = new SparseIntArray();

    private PlaceTypeMapper() {
        placeSubTypeMapping.put(Place.SUBTYPE_TEMPLE, Place.TYPE_WORSHIP);
        placeSubTypeMapping.put(Place.SUBTYPE_CHURCH, Place.TYPE_WORSHIP);
        placeSubTypeMapping.put(Place.SUBTYPE_MOSQUE, Place.TYPE_WORSHIP);
    }

    public static PlaceTypeMapper getInstance() {
        if (instances == null)
            instances = new PlaceTypeMapper();
        return instances;
    }

    public int findBySubType(int subtypeID) {
        return placeSubTypeMapping.get(subtypeID, subtypeID);
    }
}
