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
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.domain.user.UserRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.field.Location;
import th.or.nectec.tanrabad.survey.utils.time.ThaiDateTimeConverter;

import java.util.UUID;

@JsonObject
public class JsonBuilding {

    @JsonField(name = "building_id", typeConverter = UuidTypeConverter.class)
    public UUID buildingId;

    @JsonField(name = "place_id", typeConverter = UuidTypeConverter.class)
    public UUID placeId;

    @JsonField(name = "place_type_id")
    public int placeTypeId;

    @JsonField(name = "name")
    public String buildingName;

    @JsonField
    public GeoJsonPoint location;

    @JsonField(name = "updated_by")
    public String updatedBy;

    @JsonField(name = "update_timestamp")
    public String updateTime;

    @JsonField
    public boolean active = true;

    public static JsonBuilding parse(Building building) {
        JsonBuilding jsonBuilding = new JsonBuilding();
        jsonBuilding.buildingId = building.getId();
        jsonBuilding.placeId = building.getPlace().getId();
        jsonBuilding.buildingName = building.getName();
        jsonBuilding.placeTypeId = building.getPlace().getType();
        jsonBuilding.location = GeoJsonPoint.parse(building.getLocation());
        jsonBuilding.updatedBy = building.getUpdateBy();
        jsonBuilding.updateTime = building.getUpdateTimestamp().withZone(DateTimeZone.UTC).toString();
        return jsonBuilding;
    }

    public Building getEntity(PlaceRepository placeRepository, UserRepository userRepository) {
        Building building = new Building(buildingId, buildingName);
        building.setPlace(placeRepository.findByUuid(placeId));
        Location location = this.location == null ? null : this.location.getEntity();
        building.setLocation(location);
        building.setUpdateBy(updatedBy);
        building.setUpdateTimestamp(ThaiDateTimeConverter.convert(updateTime).toString());
        return building;
    }
}
