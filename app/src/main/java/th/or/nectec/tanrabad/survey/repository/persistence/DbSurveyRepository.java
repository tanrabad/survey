package th.or.nectec.tanrabad.survey.repository.persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import th.or.nectec.tanrabad.domain.UserRepository;
import th.or.nectec.tanrabad.domain.building.BuildingRepository;
import th.or.nectec.tanrabad.domain.survey.ContainerTypeRepository;
import th.or.nectec.tanrabad.domain.survey.SurveyRepository;
import th.or.nectec.tanrabad.entity.*;
import th.or.nectec.tanrabad.survey.repository.BrokerBuildingRepository;
import th.or.nectec.tanrabad.survey.repository.StubUserRepository;
import th.or.nectec.tanrabad.survey.utils.collection.CursorMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DbSurveyRepository implements SurveyRepository{

    private Context context;
    private UserRepository userRepository;
    private BuildingRepository buildingRepository;
    private ContainerTypeRepository containerTypeRepository;

    public static String TABLE_NAME = "survey";
    public static String DETAIL_TABLE_NAME = "survey_detail";

    public DbSurveyRepository(Context context) {
        this(context, new StubUserRepository(), BrokerBuildingRepository.getInstance(), new DbContainerTypeRepository(context));
    }

    public DbSurveyRepository(Context context, UserRepository userRepository, BuildingRepository buildingRepository, ContainerTypeRepository containerTypeRepository ) {
        this.context = context;
        this.userRepository = userRepository;
        this.buildingRepository = buildingRepository;
        this.containerTypeRepository = containerTypeRepository;
    }

    @Override
    public boolean save(Survey survey) {
        return false;
    }

    @Override
    public Survey findByBuildingAndUserIn7Day(Building building, User user) {
        return null;
    }

    @Override
    public ArrayList<Survey> findByPlaceAndUserIn7Days(Place place, User user) {
        return null;
    }

    @Override
    public List<SurveyDetail> getSurveyDetail(UUID surveyId, int containerLocationID) {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        Cursor cursor = db.query(DETAIL_TABLE_NAME, BuildingColumn.wildcard(),
                SurveyDetailColumn.SURVEY_ID + "=? AND " + SurveyDetailColumn.CONTAINER_LOCATION_ID + "=?", new String[]{surveyId.toString(), String.valueOf(containerLocationID)}, null, null, null);
        cursor.close();
        return surveyDetails;
    }

    private CursorMapper<Survey> getSurveyMapper(Cursor cursor) {
        return new SurveyCursorMapper(cursor, userRepository, buildingRepository, this);
    }

    private CursorMapper<SurveyDetail> getSurveyDetailMapper(Cursor cursor){
        return new SurveyDetailCursorMapper(cursor, containerTypeRepository);
    }

    @Override
    public ArrayList<Place> findByUserIn7Days(User user) {
        return null;
    }
}
