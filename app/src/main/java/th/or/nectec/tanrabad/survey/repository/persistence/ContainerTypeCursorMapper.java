package th.or.nectec.tanrabad.survey.repository.persistence;

import android.database.Cursor;
import th.or.nectec.tanrabad.entity.lookup.ContainerType;
import th.or.nectec.tanrabad.survey.utils.collection.CursorMapper;

public class ContainerTypeCursorMapper implements CursorMapper<ContainerType> {

    private int idIndex;
    private int nameIndex;

    public ContainerTypeCursorMapper(Cursor cursor) {
        findColumnIndexOf(cursor);
    }

    private void findColumnIndexOf(Cursor cursor) {
        idIndex = cursor.getColumnIndex(ContainerTypeColumn.ID);
        nameIndex = cursor.getColumnIndex(ContainerTypeColumn.NAME);
    }

    @Override
    public ContainerType map(Cursor cursor) {
        return new ContainerType(cursor.getInt(idIndex), cursor.getString(nameIndex));
    }
}
