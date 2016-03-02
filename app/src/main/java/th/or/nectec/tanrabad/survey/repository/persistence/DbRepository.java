package th.or.nectec.tanrabad.survey.repository.persistence;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DbRepository {

    private Context context;
    private SQLiteDatabase readableDatabase;
    private SQLiteDatabase writableDatabase;

    public DbRepository(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    protected SQLiteDatabase readableDatabase() {
        if (readableDatabase == null || !readableDatabase.isOpen())
            readableDatabase = new SurveyLiteDatabase(context).getReadableDatabase();
        return readableDatabase;
    }

    protected SQLiteDatabase writableDatabase() {
        if (writableDatabase == null || !writableDatabase.isOpen())
            writableDatabase = new SurveyLiteDatabase(context).getReadableDatabase();
        return writableDatabase;
    }
}
