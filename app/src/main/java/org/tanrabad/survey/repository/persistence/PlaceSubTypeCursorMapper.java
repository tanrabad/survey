package org.tanrabad.survey.repository.persistence;

import android.database.Cursor;
import org.tanrabad.survey.utils.collection.CursorMapper;
import th.or.nectec.tanrabad.entity.lookup.PlaceSubType;

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
