package org.tanrabad.survey.repository.persistence;

import android.database.Cursor;
import org.tanrabad.survey.utils.collection.CursorMapper;
import org.tanrabad.survey.entity.lookup.PlaceType;

public class PlaceTypeCursorMapper implements CursorMapper<PlaceType> {

    private int idIndex;
    private int nameIndex;

    public PlaceTypeCursorMapper(Cursor cursor) {
        findColumnIndexOf(cursor);
    }

    private void findColumnIndexOf(Cursor cursor) {
        idIndex = cursor.getColumnIndex(PlaceTypeColumn.ID);
        nameIndex = cursor.getColumnIndex(PlaceTypeColumn.NAME);
    }

    @Override
    public PlaceType map(Cursor cursor) {
        return new PlaceType(cursor.getInt(idIndex), cursor.getString(nameIndex));
    }
}
