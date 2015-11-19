package th.or.nectec.tanrabad.domain;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.Place;

public class PlaceSaverTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private UUID placeUUID;
    private String placeName;
    private int placeType;
    private Location placeLocation;

    private PlaceValidator placeValidator;
    private PlaceSavePresenter placeSavePresenter;
    private PlaceRepository placeRepository;
    private Place place;

    @Before
    public void setUp() {
        placeSavePresenter = context.mock(PlaceSavePresenter.class);
        placeValidator = context.mock(PlaceValidator.class);
        placeRepository = context.mock(PlaceRepository.class);

        placeUUID = UUID.nameUUIDFromBytes("1stu".getBytes());
        placeName = "victoria";
        placeType = Place.TYPE_FACTORY;
        placeLocation = new Location(51.500152, -0.126236);

        place = new Place(placeUUID, placeName);
        place.setType(placeType);
        place.setLocation(placeLocation);
    }

    @Test
    public void testHappyPath() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(PlaceSaverTest.this.placeValidator).validate(with(place));
                will(returnValue(true));
                oneOf(PlaceSaverTest.this.placeRepository).save(with(place));
                will(returnValue(true));
                oneOf(PlaceSaverTest.this.placeSavePresenter).displaySaveSuccess();
            }
        });
        PlaceSaver placeSaver = new PlaceSaver(placeSavePresenter, placeValidator, placeRepository);
        placeSaver.save(place);
    }

    @Test
    public void testSadPath() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(placeValidator).validate(place);
                will(returnValue(false));
                never(placeRepository);
                oneOf(placeSavePresenter).displaySaveFail();
            }
        });
        PlaceSaver placeSaver = new PlaceSaver(placeSavePresenter, placeValidator, placeRepository);
        placeSaver.save(place);
    }

    @Test
    public void testCannotSaveVillageType() throws Exception {

        place = Place.withName("123");
        place.setType(Place.TYPE_VILLAGE_COMMUNITY);

        context.checking(new Expectations() {
            {
                allowing(placeValidator).validate(place);
                will(returnValue(false));
                never(placeRepository);
                oneOf(placeSavePresenter).alertCannotSaveVillageType();
            }
        });
        PlaceSaver placeSaver = new PlaceSaver(placeSavePresenter, placeValidator, placeRepository);
        placeSaver.save(place);
    }
}
