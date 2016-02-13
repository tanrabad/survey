/*
 * Copyright (c) 2016 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import org.joda.time.DateTimeZone;
import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.field.Location;
import th.or.nectec.tanrabad.survey.repository.persistence.PlaceTypeMapper;
import th.or.nectec.tanrabad.survey.utils.time.ThaiDateTimeConverter;

import java.util.UUID;

@JsonObject
public class JsonPlace {

    @JsonField(name = "place_id", typeConverter = UuidTypeConverter.class)
    public UUID placeID;

    @JsonField(name = "place_type_id")
    public int placeTypeID;

    @JsonField(name = "place_subtype_id")
    public int placeSubtypeID;

    @JsonField(name = "place_name")
    public String placeName;

    @JsonField(name = "tambon_code")
    public String tambonCode;

    @JsonField(name = "tambon_name")
    public String tambonName;

    @JsonField(name = "amphur_code")
    public String amphurCode;

    @JsonField(name = "amphur_name")
    public String amphurName;

    @JsonField(name = "province_code")
    public String provinceCode;

    @JsonField(name = "province_name")
    public String provinceName;

    @JsonField
    public GeoJsonPoint location;

    @JsonField(name = "updated_by")
    public String updatedBy;

    @JsonField(name = "update_timestamp")
    public String updateTime;

    public static JsonPlace parse(Place place) {
        JsonPlace jsonPlace = new JsonPlace();
        jsonPlace.placeID = place.getId();
        jsonPlace.placeName = place.getName();
        jsonPlace.placeTypeID = place.getType();
        jsonPlace.placeSubtypeID = place.getSubType();
        jsonPlace.location = GeoJsonPoint.parse(place.getLocation());
        jsonPlace.tambonCode = place.getSubdistrictCode();
        jsonPlace.updatedBy = place.getUpdateBy();
        jsonPlace.updateTime = place.getUpdateTimestamp().withZone(DateTimeZone.UTC).toString();
        return jsonPlace;
    }

    public Place getEntity(UserRepository userRepository) {
        Place place = new Place(placeID, placeName);
        place.setType(placeTypeID);
        place.setSubType(placeSubtypeID == 0
                ? PlaceTypeMapper.getInstance().getDefaultPlaceType(placeTypeID) : placeSubtypeID);
        place.setSubdistrictCode(tambonCode);
        Location location = this.location == null ? null : this.location.getEntity();
        place.setLocation(location);
        place.setUpdateBy(updatedBy);
        place.setUpdateTimestamp(ThaiDateTimeConverter.convert(updateTime).toString());
        return place;
    }

}
