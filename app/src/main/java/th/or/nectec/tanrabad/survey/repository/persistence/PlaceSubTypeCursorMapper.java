package th.or.nectec.tanrabad.survey.repository.persistence;

import android.database.Cursor;
import th.or.nectec.tanrabad.entity.lookup.PlaceSubType;
import th.or.nectec.tanrabad.survey.utils.collection.CursorMapper;

public class PlaceSubTypeCursorMapper implements CursorMapper<PlaceSubType> {

    private int idIndex;
    private int nameIndex;
    private int typeIdIndex;

    public PlaceSubTypeCursorMapper(Cursor cursor) {
        findColumnIndexOf(cursor);
    }

    private void findColumnIndexOf(Cursor cursor) {
        idIndex = cursor.getColumnIndex(PlaceSubTypeColumn.ID);
        nameIndex = cursor.getColumnIndex(PlaceSubTypeColumn.NAME);
        typeIdIndex = cursor.getColumnIndex(PlaceSubTypeColumn.TYPE_ID);
    }

    @Override
    public PlaceSubType map(Cursor cursor) {
        return new PlaceSubType(cursor.getInt(idIndex), cursor.getString(nameIndex), cursor.getInt(typeIdIndex));
    }
}
