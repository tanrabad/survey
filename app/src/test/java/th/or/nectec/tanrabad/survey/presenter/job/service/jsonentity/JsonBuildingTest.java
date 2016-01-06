package th.or.nectec.tanrabad.survey.presenter.job.service.jsonentity;

import android.support.annotation.NonNull;
import com.bluelinelabs.logansquare.LoganSquare;
import com.google.gson.Gson;
import org.junit.Test;
import org.mockito.Mockito;
import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.domain.place.PlaceRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.User;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class JsonBuildingTest {

    private static final String rawBuildingString = "{" +
            "\"building_id\":\"5cf5665b-5642-10fb-a3a0-5e612a842583\"," +
            "\"place_id\":\"5cf5665b-5642-10fb-a3a0-5e612a842584\"," +
            "\"place_type_id\":4," +
            "\"name\":\"อาคาร 1\"," +
            "\"location\":{ \"type\": \"Point\", \"coordinates\": [-73.15005, 39.745673]}," +
            "\"update_by\":\"dcp-user\"" +
            "}";

    private Gson gson = new Gson();

    @Test
    public void testParseToJsonString() throws Exception {
        JsonBuilding jsonBuilding = LoganSquare.parse(rawBuildingString, JsonBuilding.class);

        assertEquals("5cf5665b-5642-10fb-a3a0-5e612a842583", jsonBuilding.buildingID.toString());
        assertEquals("5cf5665b-5642-10fb-a3a0-5e612a842584", jsonBuilding.placeID.toString());
        assertEquals(4, jsonBuilding.placeTypeID);
        assertEquals("อาคาร 1", jsonBuilding.buildingName);
        assertEquals(39.745673, jsonBuilding.location.getLatitude(), 0);
        assertEquals(-73.15005, jsonBuilding.location.getLongitude(), 0);
        assertEquals(jsonBuilding.updateBy, jsonBuilding.updateBy);
    }

    @Test
    public void testParseBuildingDataToJsonString() throws Exception {
        Building buildingData = new Building(UUID.nameUUIDFromBytes("123".getBytes()), "อาคาร 2");
        buildingData.setPlace(stubPlace());
        buildingData.setLocation(stubLocation());
        buildingData.setUpdateBy(stubUser());

        JsonBuilding jsonBuilding = JsonBuilding.parse(buildingData);

        assertEquals(UUID.nameUUIDFromBytes("123".getBytes()), jsonBuilding.buildingID);
        assertEquals(stubPlace().getId(), jsonBuilding.placeID);
        assertEquals(Place.TYPE_HOSPITAL, jsonBuilding.placeTypeID);
        assertEquals("อาคาร 2", jsonBuilding.buildingName);
        assertEquals(39.745673, jsonBuilding.location.getLatitude(), 0);
        assertEquals(-73.15005, jsonBuilding.location.getLongitude(), 0);
        assertEquals(jsonBuilding.updateBy, stubUser().getUsername());
    }

    @NonNull
    private Place stubPlace() {
        Place place = new Place(UUID.fromString("5cf5665b-5642-10fb-a3a0-5e612a842584"), "รพ.สต.ตำบลนาทราย");
        place.setType(Place.TYPE_HOSPITAL);
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
        Mockito.when(userRepository.findUserByName("dcp-user")).thenReturn(stubUser());
        PlaceRepository placeRepository = Mockito.mock(PlaceRepository.class);
        Mockito.when(placeRepository.findPlaceByUUID(stubPlace().getId())).thenReturn(stubPlace());
        Building buildingData = new Building(UUID.fromString("5cf5665b-5642-10fb-a3a0-5e612a842583"), "อาคาร 1");
        buildingData.setPlace(stubPlace());
        buildingData.setLocation(stubLocation());
        buildingData.setUpdateBy(stubUser());
        JsonBuilding jsonBuilding = LoganSquare.parse(rawBuildingString, JsonBuilding.class);
        Building parsedBuilding = jsonBuilding.getEntity(placeRepository, userRepository);

        assertEquals(parsedBuilding, buildingData);
    }
}