package org.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import th.or.nectec.tanrabad.entity.lookup.PlaceSubType;

@JsonObject
public class JsonPlaceSubType {

    @JsonField(name = "place_subtype_id")
    public int placeSubTypeId;

    @JsonField(name = "place_subtype_name")
    public String placeSubTypeName;

    @JsonField(name = "place_type_id")
    public int placeTypeId;

    public PlaceSubType getEntity() {
        return new PlaceSubType(placeSubTypeId, placeSubTypeName, placeTypeId);
    }
}
