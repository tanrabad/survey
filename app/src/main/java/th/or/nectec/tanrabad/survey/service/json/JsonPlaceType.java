package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import th.or.nectec.tanrabad.entity.lookup.PlaceType;

@JsonObject
public class JsonPlaceType {

    @JsonField(name = "place_type_id")
    public int placeTypeID;

    @JsonField(name = "place_type_name")
    public String placeTypeName;

    public PlaceType getEntity() {
        return new PlaceType(placeTypeID, placeTypeName);
    }
}
