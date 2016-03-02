package th.or.nectec.tanrabad.survey.repository.persistence;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DbRepository {

    private static SQLiteDatabase readableDatabase;
    private static SQLiteDatabase writableDatabase;
    private Context context;

    public DbRepository(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    protected SQLiteDatabase readableDatabase() {
        if (readableDatabase == null || !readableDatabase.isOpen())
            readableDatabase = SurveyLiteDatabase.getInstance(context).getReadableDatabase();
        return readableDatabase;
    }

    protected SQLiteDatabase writableDatabase() {
        if (writableDatabase == null || !writableDatabase.isOpen())
            writableDatabase = SurveyLiteDatabase.getInstance(context).getReadableDatabase();
        return writableDatabase;
    }
}
