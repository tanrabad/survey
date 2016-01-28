package th.or.nectec.tanrabad.survey.repository.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import th.or.nectec.tanrabad.domain.survey.ContainerLocationRepository;
import th.or.nectec.tanrabad.entity.lookup.ContainerLocation;
import th.or.nectec.tanrabad.survey.utils.collection.CursorList;
import th.or.nectec.tanrabad.survey.utils.collection.CursorMapper;

import java.util.List;

public class DbContainerLocationRepository implements ContainerLocationRepository {

    public static final String TABLE_NAME = "container_location";
    public static final int ERROR_INSERT_ID = -1;

    private Context context;

    public DbContainerLocationRepository(Context context) {
        this.context = context;
    }

    @Override
    public List<ContainerLocation> find() {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, ContainerLocationColumn.wildcard(),
                null, null, null, null, ContainerLocationColumn.ID);
        return new CursorList<>(cursor, getMapper(cursor));
    }

    @Override
    public ContainerLocation findByID(int containerTypeID) {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, ContainerLocationColumn.wildcard(),
                null, null, null, null, ContainerLocationColumn.ID);
        return getContainerLocation(cursor);
    }

    private ContainerLocation getContainerLocation(Cursor cursor) {
        if (cursor.moveToFirst()) {
            ContainerLocation place = getMapper(cursor).map(cursor);
            cursor.close();
            return place;
        } else {
            cursor.close();
            return null;
        }
    }

    private CursorMapper<ContainerLocation> getMapper(Cursor cursor) {
        return new ContainerLocationCursorMapper(cursor);
    }

    @Override
    public boolean save(ContainerLocation containerLocation) {
        return saveByContentValues(new SurveyLiteDatabase(context).getWritableDatabase(), containerLocationContentValues(containerLocation));
    }

    @Override
    public boolean update(ContainerLocation containerLocation) {
        return updateByContentValues(new SurveyLiteDatabase(context).getWritableDatabase(), containerLocationContentValues(containerLocation));
    }

    @Override
    public void updateOrInsert(List<ContainerLocation> updateList) {
        SQLiteDatabase db = new SurveyLiteDatabase(context).getWritableDatabase();
        db.beginTransaction();
        for (ContainerLocation containerLocation : updateList) {
            ContentValues values = containerLocationContentValues(containerLocation);
            boolean updated = updateByContentValues(db, values);
            if (!updated)
                saveByContentValues(db, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    private boolean updateByContentValues(SQLiteDatabase db, ContentValues containerLocation) {
        return db.update(TABLE_NAME, containerLocation, ContainerLocationColumn.ID + "=?", new String[]{containerLocation.getAsString(ContainerLocationColumn.ID)}) > 0;
    }

    private boolean saveByContentValues(SQLiteDatabase db, ContentValues containerLocation) {
        return db.insert(TABLE_NAME, null, containerLocation) != ERROR_INSERT_ID;
    }

    private ContentValues containerLocationContentValues(ContainerLocation containerLocation) {
        ContentValues values = new ContentValues();
        values.put(ContainerLocationColumn.ID, containerLocation.getId());
        values.put(ContainerLocationColumn.NAME, containerLocation.getName());
        return values;
    }
}
