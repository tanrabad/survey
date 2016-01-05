package th.or.nectec.tanrabad.survey.presenter.job.service.jsonentity;

import com.bluelinelabs.logansquare.LoganSquare;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.entity.utils.Address;

import static org.junit.Assert.assertEquals;

public class JsonPlaceTest {

    private static final String rawPlaceString = "{" +
            "  \"place_id\": \"b7a9d934-04fc-a22e-0539-6c17504f732e\"," +
            "  \"place_type_id\": 4," +
            "  \"place_subtype_id\": 3," +
            "  \"place_namet\": \"รพ.สต.ตำบลนาทราย\"," +
            "  \"tambon_code\": \"510403\"," +
            "  \"location\":{" +
            "    \"latitude\":39.745675," +
            "    \"longitude\":-73.150055" +
            "   }," +
            "  \"update_by\":\"dcp-user\"" +
            "}";

    private Gson gson = new Gson();

    @Test
    public void testParseToJsonString() throws Exception {
        com.google.gson.JsonObject jsonObject = (com.google.gson.JsonObject) new JsonParser().parse(rawPlaceString);
        JsonPlace jsonPlace = LoganSquare.parse(rawPlaceString, JsonPlace.class);

        assertEquals(jsonPlace.placeID, UUID.fromString(jsonObject.get("place_id").getAsString()));
        assertEquals(jsonPlace.placeTypeID, jsonObject.get("place_type_id").getAsInt());
        assertEquals(jsonPlace.placeSubtypeID, jsonObject.get("place_subtype_id").getAsInt());
        assertEquals(jsonPlace.placeName, jsonObject.get("place_namet").getAsString());
        assertEquals(jsonPlace.tambonCode, jsonObject.get("tambon_code").getAsString());
        assertEquals(jsonPlace.location.toString(), jsonObject.get("location").getAsJsonObject().toString());
        assertEquals(jsonPlace.updateBy, jsonObject.get("update_by").getAsString());
    }

    @Test
    public void testParsePlaceDataToJsonString() throws Exception {
        Place placeData = new Place(UUID.nameUUIDFromBytes("123".getBytes()), "วัดป่า");
        placeData.setType(Place.TYPE_WORSHIP);
        placeData.setSubType(Place.SUBTYPE_TEMPLE);
        placeData.setAddress(stubAddress());
        placeData.setLocation(stubLocation());
        placeData.setUpdateBy(stubUser());

        JsonPlace jsonPlace = JsonPlace.parse(placeData);

        assertEquals(jsonPlace.placeID, UUID.nameUUIDFromBytes("123".getBytes()));
        assertEquals(jsonPlace.placeTypeID, Place.TYPE_WORSHIP);
        assertEquals(jsonPlace.placeSubtypeID, Place.SUBTYPE_TEMPLE);
        assertEquals(jsonPlace.placeName, "วัดป่า");
        assertEquals(jsonPlace.tambonCode, stubAddress().getAddressCode());
        assertEquals(jsonPlace.location.toString(), gson.toJson(stubLocation()));
        assertEquals(jsonPlace.updateBy, stubUser().getUsername());
    }

    private Address stubAddress() {
        Address address = new Address();
        address.setAddressCode("510403");
        return address;
    }

    private Location stubLocation() {
        return new Location(39.745675, -73.150055);
    }

    private User stubUser() {
        return new User("dcp-user");
    }

    @Test
    public void testParseJsonStringToPlaceEntity() throws Exception {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        Mockito.when(userRepository.findUserByName("dcp-user")).thenReturn(stubUser());
        Place placeData = new Place(UUID.fromString("b7a9d934-04fc-a22e-0539-6c17504f732e"), "รพ.สต.ตำบลนาทราย");
        placeData.setType(Place.TYPE_HOSPITAL);
        placeData.setSubType(3);
        placeData.setAddress(stubAddress());
        placeData.setLocation(stubLocation());
        placeData.setUpdateBy(stubUser());
        JsonPlace jsonPlace = LoganSquare.parse(rawPlaceString, JsonPlace.class);
        Place parsedPlace = jsonPlace.getEntity(userRepository);

        assertEquals(parsedPlace, placeData);
    }
}