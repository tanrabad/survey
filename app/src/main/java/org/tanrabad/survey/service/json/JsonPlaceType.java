package org.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import org.tanrabad.survey.entity.lookup.PlaceType;

@JsonObject
public class JsonPlaceType {

    @JsonField(name = "place_type_id")
    public int placeTypeId;

    @JsonField(name = "place_type_name")
    public String placeTypeName;

    public PlaceType getEntity() {
        return new PlaceType(placeTypeId, placeTypeName);
    }
}
