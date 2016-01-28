package th.or.nectec.tanrabad.survey.repository.persistence;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.domain.building.BuildingRepository;
import th.or.nectec.tanrabad.domain.survey.ContainerTypeRepository;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.SurveyDetail;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.entity.field.Location;
import th.or.nectec.tanrabad.entity.lookup.ContainerType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DbSurveyRepositoryTest {
    @Rule
    public SurveyDbTestRule dbTestRule = new SurveyDbTestRule();
    private DbSurveyRepository dbSurveyRepository;

    @Before
    public void setup() {
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        Mockito.when(userRepository.findByUsername("dpc-user")).thenReturn(stubUser());

        BuildingRepository buildingRepository = Mockito.mock(BuildingRepository.class);
        Mockito.when(buildingRepository.findByUUID(UUID.fromString("f5bfd399-8fb2-4a69-874a-b40495f7786f"))).thenReturn(stubBuilding());

        ContainerTypeRepository containerTypeRepository = Mockito.mock(ContainerTypeRepository.class);
        Mockito.when(containerTypeRepository.findByID(1)).thenReturn(getWater());
        Mockito.when(containerTypeRepository.findByID(2)).thenReturn(getDrinkingWater());

        Context context = InstrumentationRegistry.getTargetContext();
        dbSurveyRepository = new DbSurveyRepository(context, userRepository, buildingRepository, containerTypeRepository);
    }

    private Building stubBuilding() {
        return new Building(UUID.fromString("f5bfd399-8fb2-4a69-874a-b40495f7786f"), "ABC");
    }

    private ContainerType getWater() {
        return new ContainerType(1, "น้ำใช้");
    }

    private ContainerType getDrinkingWater() {
        return new ContainerType(2, "น้ำดื่ม");
    }

    private User stubUser() {
        return new User("dpc-user");
    }

    @Test
    public void testSaveSuccess() throws Exception {
        Survey survey = getSurvey();
        boolean isSuccess = dbSurveyRepository.save(survey);

        Cursor surveyQuery = dbTestRule.getReadable().query(DbSurveyRepository.TABLE_NAME, SurveyColumn.wildcard(), SurveyColumn.ID + "=?",
                new String[]{surveyID().toString()}, null, null, null);

        Cursor surveyDetails = dbTestRule.getReadable().query(DbSurveyRepository.DETAIL_TABLE_NAME, SurveyDetailColumn.wildcard(), SurveyDetailColumn.SURVEY_ID + "=?",
                new String[]{surveyID().toString()}, null, null, null);

        assertEquals(true, surveyQuery.moveToFirst());
        assertEquals(true, isSuccess);
        assertEquals(surveyID().toString(), surveyQuery.getString(surveyQuery.getColumnIndex(SurveyColumn.ID)));
        assertEquals(80f, surveyQuery.getDouble(surveyQuery.getColumnIndex(SurveyColumn.LATITUDE)), 0);
        assertEquals(12f, surveyQuery.getDouble(surveyQuery.getColumnIndex(SurveyColumn.LONGITUDE)), 0);
        assertEquals("dpc-user", surveyQuery.getString(surveyQuery.getColumnIndex(SurveyColumn.SURVEYOR)));
        assertEquals(15, surveyQuery.getInt(surveyQuery.getColumnIndex(SurveyColumn.PERSON_COUNT)));
        assertEquals("2015-12-24T12:19:20.626+07:00", surveyQuery.getString(surveyQuery.getColumnIndex(SurveyColumn.CREATE_TIME)));
        assertEquals("2015-12-24T13:20:21.626+07:00", surveyQuery.getString(surveyQuery.getColumnIndex(SurveyColumn.UPDATE_TIME)));
        assertEquals(ChangedStatus.ADD, surveyQuery.getInt(surveyQuery.getColumnIndex(SurveyColumn.CHANGED_STATUS)));

        assertEquals(3, surveyDetails.getCount());
        assertEquals(true, surveyDetails.moveToFirst());
        assertEquals(1, surveyDetails.getInt(surveyDetails.getColumnIndex(SurveyDetailColumn.CONTAINER_TYPE_ID)));
        assertEquals(1, surveyDetails.getInt(surveyDetails.getColumnIndex(SurveyDetailColumn.CONTAINER_LOCATION_ID)));
        assertEquals(true, surveyDetails.moveToNext());
        assertEquals(1, surveyDetails.getInt(surveyDetails.getColumnIndex(SurveyDetailColumn.CONTAINER_TYPE_ID)));
        assertEquals(2, surveyDetails.getInt(surveyDetails.getColumnIndex(SurveyDetailColumn.CONTAINER_LOCATION_ID)));
        assertEquals(true, surveyDetails.moveToNext());
        assertEquals(2, surveyDetails.getInt(surveyDetails.getColumnIndex(SurveyDetailColumn.CONTAINER_TYPE_ID)));
        assertEquals(2, surveyDetails.getInt(surveyDetails.getColumnIndex(SurveyDetailColumn.CONTAINER_LOCATION_ID)));
        surveyQuery.close();
        surveyDetails.close();
    }

    private Survey getSurvey() {
        Survey survey = new Survey(surveyID(), stubUser(), stubBuilding());
        survey.setLocation(new Location(80, 12));
        survey.setResidentCount(15);
        SurveyDetail surveyDetail1 = new SurveyDetail(UUID.randomUUID(), getWater(), 3, 2);
        SurveyDetail surveyDetail3 = new SurveyDetail(UUID.randomUUID(), getWater(), 6, 5);
        SurveyDetail surveyDetail4 = new SurveyDetail(UUID.randomUUID(), getDrinkingWater(), 4, 1);
        List<SurveyDetail> indoorDetail = new ArrayList<>();
        indoorDetail.add(surveyDetail1);
        survey.setIndoorDetail(indoorDetail);
        List<SurveyDetail> outdoorDetail = new ArrayList<>();
        outdoorDetail.add(surveyDetail3);
        outdoorDetail.add(surveyDetail4);
        survey.setOutdoorDetail(outdoorDetail);
        survey.setStartTimestamp(new DateTime("2015-12-24T12:19:20.626+07:00"));
        survey.setFinishTimestamp(new DateTime("2015-12-24T13:20:21.626+07:00"));
        return survey;
    }

    private UUID surveyID() {
        return UUID.nameUUIDFromBytes("abc".getBytes());
    }

    @Test
    public void testUpdate() throws Exception {
        Survey survey = getSurvey();
        dbSurveyRepository.save(survey);
        survey.setResidentCount(6);
        List<SurveyDetail> indoorDetail = survey.getIndoorDetail();
        indoorDetail.get(0).setContainerCount(5, 4);
        indoorDetail.add(new SurveyDetail(UUID.randomUUID(), getDrinkingWater(), 7, 3));
        survey.setIndoorDetail(indoorDetail);
        boolean isSuccess = dbSurveyRepository.update(survey);

        Cursor surveyQuery = dbTestRule.getReadable().query(DbSurveyRepository.TABLE_NAME, SurveyColumn.wildcard(), SurveyColumn.ID + "=?",
                new String[]{survey.getId().toString()}, null, null, null);

        assertEquals(true, isSuccess);
        assertEquals(true, surveyQuery.moveToFirst());
        assertEquals(6, surveyQuery.getInt(surveyQuery.getColumnIndex(SurveyColumn.PERSON_COUNT)));
        assertEquals(ChangedStatus.ADD, surveyQuery.getInt(surveyQuery.getColumnIndex(SurveyColumn.CHANGED_STATUS)));

        List<SurveyDetail> surveyDetails = dbSurveyRepository.findSurveyDetail(survey.getId(), DbSurveyRepository.INDOOR_CONTAINER_LOCATION);
        assertEquals(2, surveyDetails.size());

        Cursor surveyDetailsCursor = dbTestRule.getReadable().query(DbSurveyRepository.DETAIL_TABLE_NAME, SurveyDetailColumn.wildcard(), SurveyDetailColumn.SURVEY_ID + "=?",
                new String[]{surveyID().toString()}, null, null, null);

        assertEquals(4, surveyDetailsCursor.getCount());
        assertEquals(true, surveyDetailsCursor.moveToFirst());
        assertEquals(1, surveyDetailsCursor.getInt(surveyDetailsCursor.getColumnIndex(SurveyDetailColumn.CONTAINER_TYPE_ID)));
        assertEquals(1, surveyDetailsCursor.getInt(surveyDetailsCursor.getColumnIndex(SurveyDetailColumn.CONTAINER_LOCATION_ID)));
        assertEquals(true, surveyDetailsCursor.moveToNext());
        assertEquals(1, surveyDetailsCursor.getInt(surveyDetailsCursor.getColumnIndex(SurveyDetailColumn.CONTAINER_TYPE_ID)));
        assertEquals(2, surveyDetailsCursor.getInt(surveyDetailsCursor.getColumnIndex(SurveyDetailColumn.CONTAINER_LOCATION_ID)));
        assertEquals(true, surveyDetailsCursor.moveToNext());
        assertEquals(2, surveyDetailsCursor.getInt(surveyDetailsCursor.getColumnIndex(SurveyDetailColumn.CONTAINER_TYPE_ID)));
        assertEquals(2, surveyDetailsCursor.getInt(surveyDetailsCursor.getColumnIndex(SurveyDetailColumn.CONTAINER_LOCATION_ID)));
        surveyQuery.close();
        surveyDetailsCursor.close();
    }

    @Test
    public void testUpdateException() throws Exception {
        Survey survey = getSurvey();
        dbSurveyRepository.save(survey);
        List<SurveyDetail> indoorDetail = new ArrayList<>();
        indoorDetail.add(new SurveyDetail(UUID.randomUUID(), getWater(), 3, 2));
        survey.setIndoorDetail(indoorDetail);
        boolean isSuccess = dbSurveyRepository.update(survey);

        List<SurveyDetail> surveyDetails = dbSurveyRepository.findSurveyDetail(survey.getId(), DbSurveyRepository.INDOOR_CONTAINER_LOCATION);
        assertEquals(false, isSuccess);
        assertEquals(1, surveyDetails.size());
    }

    @Test
    public void testLoadSavedSurvey() throws Exception {
        Survey survey = getSurvey();
        dbSurveyRepository.save(survey);

        Survey querySurvey = dbSurveyRepository.findByBuildingAndUserIn7Day(survey.getSurveyBuilding(), survey.getUser());
        assertEquals(survey, querySurvey);
    }
}
