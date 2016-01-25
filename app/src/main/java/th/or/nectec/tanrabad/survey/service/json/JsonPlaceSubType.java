package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import th.or.nectec.tanrabad.entity.PlaceSubType;

@JsonObject
public class JsonPlaceSubType {

    @JsonField(name = "place_subtype_id")
    public int placeSubTypeID;

    @JsonField(name = "place_subtype_name")
    public String placeSubTypeName;

    @JsonField(name = "place_type_id")
    public int placeTypeID;

    public PlaceSubType getEntity() {
        return new PlaceSubType(placeSubTypeID, placeSubTypeName, placeTypeID);
    }
}
