package org.tanrabad.survey.repository.persistence;

import android.database.Cursor;
import org.tanrabad.survey.utils.collection.CursorMapper;
import org.tanrabad.survey.entity.lookup.ContainerLocation;

public class ContainerLocationCursorMapper implements CursorMapper<ContainerLocation> {

    private int idIndex;
    private int nameIndex;

    public ContainerLocationCursorMapper(Cursor cursor) {
        findColumnIndexOf(cursor);
    }

    private void findColumnIndexOf(Cursor cursor) {
        idIndex = cursor.getColumnIndex(ContainerLocationColumn.ID);
        nameIndex = cursor.getColumnIndex(ContainerLocationColumn.NAME);
    }

    @Override
    public ContainerLocation map(Cursor cursor) {
        return new ContainerLocation(cursor.getInt(idIndex), cursor.getString(nameIndex));
    }
}
