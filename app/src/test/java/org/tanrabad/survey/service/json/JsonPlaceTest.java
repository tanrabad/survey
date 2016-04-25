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

import com.bluelinelabs.logansquare.LoganSquare;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.tanrabad.survey.domain.place.PlaceSubTypeRepository;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.entity.field.Location;
import org.tanrabad.survey.entity.lookup.PlaceSubType;
import org.tanrabad.survey.entity.lookup.PlaceType;
import org.tanrabad.survey.utils.ResourceFile;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class JsonPlaceTest {
    private PlaceSubTypeRepository placeSubTypeRepository;

    @Before
    public void setup() {
        placeSubTypeRepository = Mockito.mock(PlaceSubTypeRepository.class);
        Mockito.when(placeSubTypeRepository.getDefaultPlaceSubTypeId(PlaceType.HOSPITAL)).thenReturn(1);
    }

    @Test
    public void testParseToJsonString() throws Exception {
        JsonPlace jsonPlace = LoganSquare.parse(ResourceFile.read("place.json"), JsonPlace.class);

        assertEquals("b7a9d934-04fc-a22e-0539-6c17504f732e", jsonPlace.placeId.toString());
        assertEquals(4, jsonPlace.placeTypeId);
        assertEquals(3, jsonPlace.placeSubtypeId);
        assertEquals("รพ.สต.ตำบลนาทราย", jsonPlace.placeName);
        assertEquals("510403", jsonPlace.tambonCode);
        assertEquals(39.745675, jsonPlace.location.getLatitude(), 0);
        assertEquals(-73.150055, jsonPlace.location.getLongitude(), 0);
        assertEquals("dcp-user", jsonPlace.updatedBy);
        assertEquals(true, jsonPlace.active);
    }

    @Test
    public void testParsePlaceDataToJsonString() throws Exception {
        Place placeData = new Place(UUID.nameUUIDFromBytes("123".getBytes()), "วัดป่า");
        placeData.setType(PlaceType.WORSHIP);
        placeData.setSubType(PlaceSubType.TEMPLE);
        placeData.setSubdistrictCode("510403");
        placeData.setLocation(stubLocation());
        placeData.setUpdateBy(stubUser());
        DateTime updateTime = DateTime.now();
        placeData.setUpdateTimestamp(updateTime.toString());

        JsonPlace jsonPlace = JsonPlace.parse(placeData);

        assertEquals(UUID.nameUUIDFromBytes("123".getBytes()), jsonPlace.placeId);
        assertEquals(PlaceType.WORSHIP, jsonPlace.placeTypeId);
        assertEquals(PlaceSubType.TEMPLE, jsonPlace.placeSubtypeId);
        assertEquals("วัดป่า", jsonPlace.placeName);
        assertEquals("510403", jsonPlace.tambonCode);
        assertEquals(stubLocation().getLatitude(), jsonPlace.location.getLatitude(), 0);
        assertEquals(stubLocation().getLongitude(), jsonPlace.location.getLongitude(), 0);
        assertEquals(stubUser().getUsername(), jsonPlace.updatedBy);
        assertEquals(updateTime.withZone(DateTimeZone.UTC).toString(), jsonPlace.updateTime);
    }


    private Location stubLocation() {
        return new Location(39.745675, -73.150055);
    }

    private User stubUser() {
        return new User("dcp-user");
    }

    @Test
    public void testParseJsonStringToPlaceEntity() throws Exception {
        Place placeData = new Place(UUID.fromString("b7a9d934-04fc-a22e-0539-6c17504f732e"), "รพ.สต.ตำบลนาทราย");
        placeData.setType(PlaceType.HOSPITAL);
        placeData.setSubType(3);
        placeData.setSubdistrictCode("510403");
        placeData.setLocation(stubLocation());
        placeData.setUpdateBy(stubUser());
        placeData.setUpdateTimestamp(DateTime.now().toString());
        JsonPlace jsonPlace = LoganSquare.parse(ResourceFile.read("place.json"), JsonPlace.class);
        Place parsedPlace = jsonPlace.getEntity(placeSubTypeRepository);
        assertEquals(parsedPlace, placeData);
        assertEquals(12, parsedPlace.getUpdateTimestamp().getHourOfDay());
    }

    @Test
    public void testParseJsonStringWithNullPlaceSubtypeToPlaceEntity() throws Exception {
        Place placeData = new Place(UUID.fromString("b7a9d934-04fc-a22e-0539-6c17504f732e"), "รพ.สต.ตำบลนาทราย");
        placeData.setType(PlaceType.HOSPITAL);
        placeData.setSubType(1);
        placeData.setSubdistrictCode("510403");
        placeData.setLocation(stubLocation());
        placeData.setUpdateBy(stubUser());
        placeData.setUpdateTimestamp(DateTime.now().toString());
        JsonPlace jsonPlace = LoganSquare.parse(ResourceFile.read("placeWithNullSubType.json"), JsonPlace.class);
        Place parsedPlace = jsonPlace.getEntity(placeSubTypeRepository);
        assertEquals(parsedPlace, placeData);
    }
}
