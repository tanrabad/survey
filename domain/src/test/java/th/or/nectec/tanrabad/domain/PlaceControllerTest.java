package th.or.nectec.tanrabad.domain;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

import th.or.nectec.tanrabad.entity.Place;

public class PlaceControllerTest {
    public final String placeName = "New York";

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    Place place = Place.withName(placeName);
    private PlaceRepository placeRepository;
    private PlacePresenter placePresenter;
    private UUID placeUUID;
    private PlaceController placeController;

    @Before
    public void setUp() throws Exception {
        placeUUID = UUID.nameUUIDFromBytes("3zyx".getBytes());

        placeRepository = context.mock(PlaceRepository.class);
        placePresenter = context.mock(PlacePresenter.class);
    }

    @Test
    public void testFoundPlace() throws Exception {
        context.checking(new Expectations() {
            {
                allowing(placeRepository).findPlaceByPlaceUUID(placeUUID);
                will(returnValue(place));
                oneOf(placePresenter).displayPlace(place);
            }
        });
        PlaceController placeController = new PlaceController(placeRepository, placePresenter);
        placeController.showPlace(placeUUID);
    }

    @Test
    public void testNotFoundPlace() throws Exception {
        context.checking(new Expectations() {
            {
            allowing(placeRepository).findPlaceByPlaceUUID(placeUUID);
                will(returnValue(null));
                oneOf(placePresenter).alertPlaceNotFound();
            }
        });
        PlaceController placeController = new PlaceController(placeRepository, placePresenter);
        placeController.showPlace(placeUUID);
    }

}
