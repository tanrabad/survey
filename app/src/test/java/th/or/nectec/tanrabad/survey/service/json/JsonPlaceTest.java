package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.LoganSquare;
import com.google.gson.Gson;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.mockito.Mockito;
import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.entity.utils.Address;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class JsonPlaceTest {

    private static final String rawPlaceString = "{" +
            "  \"place_id\": \"b7a9d934-04fc-a22e-0539-6c17504f732e\"," +
            "  \"place_type_id\": 4," +
            "  \"place_subtype_id\": 3," +
            "  \"place_name\": \"รพ.สต.ตำบลนาทราย\"," +
            "  \"tambon_code\": \"510403\"," +
            "  \"location\":{ \"type\": \"Point\", \"coordinates\": [-73.150055, 39.745675]}," +
            "  \"update_by\":\"dcp-user\"," +
            "  \"update_timestamp\": \"2015-12-24T05:05:19.626Z\"}";

    private Gson gson = new Gson();

    @Test
    public void testParseToJsonString() throws Exception {
        JsonPlace jsonPlace = LoganSquare.parse(rawPlaceString, JsonPlace.class);

        assertEquals("b7a9d934-04fc-a22e-0539-6c17504f732e", jsonPlace.placeID.toString());
        assertEquals(4, jsonPlace.placeTypeID);
        assertEquals(3, jsonPlace.placeSubtypeID);
        assertEquals("รพ.สต.ตำบลนาทราย", jsonPlace.placeName);
        assertEquals("510403", jsonPlace.tambonCode);
        assertEquals(39.745675, jsonPlace.location.getLatitude(), 0);
        assertEquals(-73.150055, jsonPlace.location.getLongitude(), 0);
        assertEquals("dcp-user", jsonPlace.updateBy);
    }

    @Test
    public void testParsePlaceDataToJsonString() throws Exception {
        Place placeData = new Place(UUID.nameUUIDFromBytes("123".getBytes()), "วัดป่า");
        placeData.setType(Place.TYPE_WORSHIP);
        placeData.setSubType(Place.SUBTYPE_TEMPLE);
        placeData.setAddress(stubAddress());
        placeData.setLocation(stubLocation());
        placeData.setUpdateBy(stubUser());
        DateTime updateTime = DateTime.now();
        placeData.setUpdateTimestamp(updateTime.toString());

        JsonPlace jsonPlace = JsonPlace.parse(placeData);

        assertEquals(UUID.nameUUIDFromBytes("123".getBytes()), jsonPlace.placeID);
        assertEquals(Place.TYPE_WORSHIP, jsonPlace.placeTypeID);
        assertEquals(Place.SUBTYPE_TEMPLE, jsonPlace.placeSubtypeID);
        assertEquals("วัดป่า", jsonPlace.placeName);
        assertEquals(stubAddress().getAddressCode(), jsonPlace.tambonCode);
        assertEquals(stubLocation().getLatitude(), jsonPlace.location.getLatitude(), 0);
        assertEquals(stubLocation().getLongitude(), jsonPlace.location.getLongitude(), 0);
        assertEquals(stubUser().getUsername(), jsonPlace.updateBy);
        assertEquals(updateTime.withZone(DateTimeZone.UTC), DateTime.parse(jsonPlace.updateTime));
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
        placeData.setUpdateTimestamp(DateTime.now().toString());
        JsonPlace jsonPlace = LoganSquare.parse(rawPlaceString, JsonPlace.class);
        Place parsedPlace = jsonPlace.getEntity(userRepository);

        assertEquals(parsedPlace, placeData);
    }
}