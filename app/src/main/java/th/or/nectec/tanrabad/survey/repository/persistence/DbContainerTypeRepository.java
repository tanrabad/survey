package th.or.nectec.tanrabad.survey.repository.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import th.or.nectec.tanrabad.domain.survey.ContainerTypeRepository;
import th.or.nectec.tanrabad.entity.lookup.ContainerType;
import th.or.nectec.tanrabad.survey.utils.collection.CursorList;
import th.or.nectec.tanrabad.survey.utils.collection.CursorMapper;

import java.util.List;

public class DbContainerTypeRepository extends DbRepository implements ContainerTypeRepository {

    public static final String TABLE_NAME = "container_type";
    public static final int ERROR_INSERT_ID = -1;

    public DbContainerTypeRepository(Context context) {
        super(context);
    }

    @Override
    public List<ContainerType> find() {
        SQLiteDatabase db = readableDatabase();
        Cursor containerTypeCursor = db.query(TABLE_NAME, ContainerTypeColumn.wildcard(),
                null, null, null, null, ContainerTypeColumn.ID);
        return new CursorList<>(containerTypeCursor, getMapper(containerTypeCursor));
    }

    @Override
    public ContainerType findByID(int containerTypeId) {
        SQLiteDatabase db = readableDatabase();
        Cursor containerTypeCursor = db.query(TABLE_NAME, ContainerTypeColumn.wildcard(),
                ContainerTypeColumn.ID + "=?", new String[]{String.valueOf(containerTypeId)}, null, null,
                ContainerTypeColumn.ID);
        return getContainerType(containerTypeCursor);
    }

    private ContainerType getContainerType(Cursor cursor) {
        if (cursor.moveToFirst()) {
            ContainerType place = getMapper(cursor).map(cursor);
            cursor.close();
            return place;
        } else {
            cursor.close();
            return null;
        }
    }

    private CursorMapper<ContainerType> getMapper(Cursor cursor) {
        return new ContainerTypeCursorMapper(cursor);
    }

    @Override
    public boolean save(ContainerType containerType) {
        return saveByContentValues(writableDatabase(), containerTypeContentValues(containerType));
    }

    @Override
    public boolean update(ContainerType containerType) {
        return updateByContentValues(writableDatabase(), containerTypeContentValues(containerType));
    }

    @Override
    public void updateOrInsert(List<ContainerType> updateList) {
        SQLiteDatabase db = writableDatabase();
        db.beginTransaction();
        for (ContainerType eachContainerType : updateList) {
            ContentValues values = containerTypeContentValues(eachContainerType);
            boolean updated = updateByContentValues(db, values);
            if (!updated)
                saveByContentValues(db, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    private boolean updateByContentValues(SQLiteDatabase db, ContentValues containerType) {
        return db.update(TABLE_NAME, containerType,
                ContainerTypeColumn.ID + "=?", new String[]{containerType.getAsString(ContainerTypeColumn.ID)}) > 0;
    }

    private boolean saveByContentValues(SQLiteDatabase db, ContentValues containerType) {
        return db.insert(TABLE_NAME, null, containerType) != ERROR_INSERT_ID;
    }

    private ContentValues containerTypeContentValues(ContainerType containerType) {
        ContentValues values = new ContentValues();
        values.put(ContainerTypeColumn.ID, containerType.getId());
        values.put(ContainerTypeColumn.NAME, containerType.getName());
        return values;
    }
}
