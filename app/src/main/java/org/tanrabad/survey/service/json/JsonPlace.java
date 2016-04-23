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

package org.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import org.joda.time.DateTimeZone;
import org.tanrabad.survey.utils.time.ThaiDateTimeConverter;
import th.or.nectec.tanrabad.domain.place.PlaceSubTypeRepository;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.field.Location;

import java.util.UUID;

@JsonObject
public class JsonPlace {

    @JsonField(name = "place_id", typeConverter = UuidTypeConverter.class)
    public UUID placeId;

    @JsonField(name = "place_type_id")
    public int placeTypeId;

    @JsonField(name = "place_subtype_id")
    public int placeSubtypeId;

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

    @JsonField
    public boolean active = true;

    public static JsonPlace parse(Place place) {
        JsonPlace jsonPlace = new JsonPlace();
        jsonPlace.placeId = place.getId();
        jsonPlace.placeName = place.getName();
        jsonPlace.placeTypeId = place.getType();
        jsonPlace.placeSubtypeId = place.getSubType();
        jsonPlace.location = place.getLocation() == null ? null : GeoJsonPoint.parse(place.getLocation());
        jsonPlace.tambonCode = place.getSubdistrictCode();
        jsonPlace.updatedBy = place.getUpdateBy();
        jsonPlace.updateTime = place.getUpdateTimestamp().withZone(DateTimeZone.UTC).toString();
        return jsonPlace;
    }

    public Place getEntity(PlaceSubTypeRepository placeSubTypeRepository) {
        Place place = new Place(placeId, placeName);
        place.setType(placeTypeId);
        place.setSubType(placeSubtypeId == 0
                ? placeSubTypeRepository.getDefaultPlaceSubTypeId(placeTypeId)
                : placeSubtypeId);
        place.setSubdistrictCode(tambonCode);
        Location location = this.location == null ? null : this.location.getEntity();
        place.setLocation(location);
        place.setUpdateBy(updatedBy);
        place.setUpdateTimestamp(ThaiDateTimeConverter.convert(updateTime).toString());
        return place;
    }

}
