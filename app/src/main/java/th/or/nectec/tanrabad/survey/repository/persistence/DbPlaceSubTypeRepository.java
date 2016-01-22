package th.or.nectec.tanrabad.survey.repository.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import th.or.nectec.tanrabad.domain.place.PlaceSubTypeRepository;
import th.or.nectec.tanrabad.entity.PlaceSubType;
import th.or.nectec.tanrabad.survey.utils.collection.CursorList;
import th.or.nectec.tanrabad.survey.utils.collection.CursorMapper;

import java.util.List;

public class DbPlaceSubTypeRepository implements PlaceSubTypeRepository {

    public static final String TABLE_NAME = "place_subtype";
    public static final int ERROR_INSERT_ID = -1;

    private Context context;

    public DbPlaceSubTypeRepository(Context context) {
        this.context = context;
    }

    @Override
    public List<PlaceSubType> find() {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        Cursor placeTypeCursor = db.query(TABLE_NAME, PlaceSubTypeColumn.wildcard(),
                null, null, null, null, null);
        return new CursorList<>(placeTypeCursor, getMapper(placeTypeCursor));
    }

    @Override
    public PlaceSubType findByID(int placeSubTypeID) {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        Cursor placeTypeCursor = db.query(TABLE_NAME, PlaceSubTypeColumn.wildcard(),
                PlaceSubTypeColumn.ID + " =?", new String[]{String.valueOf(placeSubTypeID)}, null, null, null);
        return getPlaceSubType(placeTypeCursor);
    }

    @Override
    public List<PlaceSubType> findByPlaceTypeID(int placeTypeID) {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        Cursor placeTypeCursor = db.query(TABLE_NAME, PlaceSubTypeColumn.wildcard(),
                PlaceSubTypeColumn.TYPE_ID + " =?", new String[]{String.valueOf(placeTypeID)}, null, null, null);
        return new CursorList<>(placeTypeCursor, getMapper(placeTypeCursor));
    }

    private PlaceSubType getPlaceSubType(Cursor cursor) {
        if (cursor.moveToFirst()) {
            PlaceSubType placeSubType = getMapper(cursor).map(cursor);
            cursor.close();
            return placeSubType;
        } else {
            cursor.close();
            return null;
        }
    }

    private CursorMapper<PlaceSubType> getMapper(Cursor cursor) {
        return new PlaceSubTypeCursorMapper(cursor);
    }

    @Override
    public boolean save(PlaceSubType placeType) {
        return saveByContentValues(new SurveyLiteDatabase(context).getWritableDatabase(), placeTypeContentValues(placeType));
    }

    @Override
    public boolean update(PlaceSubType placeType) {
        return updateByContentValues(new SurveyLiteDatabase(context).getWritableDatabase(), placeTypeContentValues(placeType));
    }

    @Override
    public void updateOrInsert(List<PlaceSubType> updateList) {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getWritableDatabase();
        db.beginTransaction();
        for (PlaceSubType eachPlaceSubtype : updateList) {
            ContentValues values = placeTypeContentValues(eachPlaceSubtype);
            boolean updated = updateByContentValues(db, values);
            if (!updated)
                saveByContentValues(db, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    private boolean updateByContentValues(SQLiteDatabase db, ContentValues placeSubType) {
        return db.update(TABLE_NAME, placeSubType, PlaceSubTypeColumn.ID + "=?", new String[]{placeSubType.getAsString(PlaceSubTypeColumn.ID)}) > 0;
    }

    private boolean saveByContentValues(SQLiteDatabase db, ContentValues placeSubType) {
        return db.insert(TABLE_NAME, null, placeSubType) != ERROR_INSERT_ID;
    }

    private ContentValues placeTypeContentValues(PlaceSubType placeSubType) {
        ContentValues values = new ContentValues();
        values.put(PlaceSubTypeColumn.ID, placeSubType.getId());
        values.put(PlaceSubTypeColumn.NAME, placeSubType.getName());
        values.put(PlaceSubTypeColumn.TYPE_ID, placeSubType.getPlaceTypeId());
        return values;
    }
}
