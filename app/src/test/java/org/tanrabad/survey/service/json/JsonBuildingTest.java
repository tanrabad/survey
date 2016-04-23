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

import android.support.annotation.NonNull;

import com.bluelinelabs.logansquare.LoganSquare;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

import org.tanrabad.survey.domain.place.PlaceRepository;
import org.tanrabad.survey.domain.user.UserRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.entity.field.Location;
import th.or.nectec.tanrabad.entity.lookup.PlaceType;

import static org.junit.Assert.assertEquals;

public class JsonBuildingTest {

    private static final String rawBuildingString = "{"
            + "\"building_id\":\"5cf5665b-5642-10fb-a3a0-5e612a842583\","
            + "\"place_id\":\"5cf5665b-5642-10fb-a3a0-5e612a842584\","
            + "\"place_type_id\":4,"
            + "\"name\":\"อาคาร 1\","
            + "\"location\":{ \"type\": \"Point\", \"coordinates\": [-73.15005, 39.745673]},"
            + "\"updated_by\":\"dcp-user\","
            + "\"active\": true,"
            + "\"update_timestamp\": \"2015-12-24T05:05:19.626Z\"}";

    @Test
    public void testParseToJsonString() throws Exception {
        JsonBuilding jsonBuilding = LoganSquare.parse(rawBuildingString, JsonBuilding.class);

        assertEquals("5cf5665b-5642-10fb-a3a0-5e612a842583", jsonBuilding.buildingId.toString());
        assertEquals("5cf5665b-5642-10fb-a3a0-5e612a842584", jsonBuilding.placeId.toString());
        assertEquals(4, jsonBuilding.placeTypeId);
        assertEquals("อาคาร 1", jsonBuilding.buildingName);
        assertEquals(39.745673, jsonBuilding.location.getLatitude(), 0);
        assertEquals(-73.15005, jsonBuilding.location.getLongitude(), 0);
        assertEquals("dcp-user", jsonBuilding.updatedBy);
        assertEquals(true, jsonBuilding.active);
    }

    @Test
    public void testParseBuildingDataToJsonString() throws Exception {
        Building buildingData = new Building(UUID.nameUUIDFromBytes("123".getBytes()), "อาคาร 2");
        buildingData.setPlace(stubPlace());
        buildingData.setLocation(stubLocation());
        buildingData.setUpdateBy(stubUser());
        buildingData.setUpdateTimestamp("2015-11-30T17:00:00.000Z");

        JsonBuilding jsonBuilding = JsonBuilding.parse(buildingData);

        assertEquals(UUID.nameUUIDFromBytes("123".getBytes()), jsonBuilding.buildingId);
        assertEquals(stubPlace().getId(), jsonBuilding.placeId);
        assertEquals(stubPlace().getType(), jsonBuilding.placeTypeId);
        assertEquals("อาคาร 2", jsonBuilding.buildingName);
        assertEquals(39.745673, jsonBuilding.location.getLatitude(), 0);
        assertEquals(-73.15005, jsonBuilding.location.getLongitude(), 0);
        assertEquals(stubUser().getUsername(), jsonBuilding.updatedBy);
        assertEquals("2015-11-30T17:00:00.000Z", jsonBuilding.updateTime);
    }

    @NonNull
    private Place stubPlace() {
        Place place = new Place(UUID.fromString("5cf5665b-5642-10fb-a3a0-5e612a842584"), "รพ.สต.ตำบลนาทราย");
        place.setType(PlaceType.HOSPITAL);
        return place;
    }

    private Location stubLocation() {
        return new Location(39.745673, -73.15005);
    }

    private User stubUser() {
        return new User("dcp-user");
    }

    @Test
    public void testParseJsonStringToBuildingEntity() throws Exception {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        Mockito.when(userRepository.findByUsername("dcp-user")).thenReturn(stubUser());
        PlaceRepository placeRepository = Mockito.mock(PlaceRepository.class);
        Mockito.when(placeRepository.findByUuid(stubPlace().getId())).thenReturn(stubPlace());
        Building buildingData = new Building(UUID.fromString("5cf5665b-5642-10fb-a3a0-5e612a842583"), "อาคาร 1");
        buildingData.setPlace(stubPlace());
        buildingData.setLocation(stubLocation());
        buildingData.setUpdateBy(stubUser());
        JsonBuilding jsonBuilding = LoganSquare.parse(rawBuildingString, JsonBuilding.class);
        Building parsedBuilding = jsonBuilding.getEntity(placeRepository, userRepository);

        assertEquals(parsedBuilding, buildingData);
        assertEquals(12, parsedBuilding.getUpdateTimestamp().getHourOfDay());
    }
}
