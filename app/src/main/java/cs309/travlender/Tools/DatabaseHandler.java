package cs309.travlender.Tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Administrator on 2017/10/16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    public static DatabaseContract.DBevent DBevent = new DatabaseContract.DBevent();
    private static final String COMMA = ",";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String DEFAULT = " DEFAULT 0";

    //create tables
    private static final String CREATE_EVENT=
            "create table " + DBevent.TABLE_NAME + " ("
                    +DBevent._ID + INT_TYPE + " primary key autoincrement,"
                    +DBevent.KEY_TITLE + TEXT_TYPE + COMMA
                    +DBevent.KEY_ADDTIME + INT_TYPE + COMMA
                    +DBevent.KEY_STARTTIME + INT_TYPE + COMMA
                    +DBevent.KEY_ENDTIME + INT_TYPE+ COMMA
                    +DBevent.KEY_REMINDTIME + INT_TYPE+ COMMA
                    +DBevent.KEY_LOCATION + TEXT_TYPE+ DEFAULT+ COMMA
                    +DBevent.KEY_LONGITUDE + REAL_TYPE+ DEFAULT+ COMMA
                    +DBevent.KEY_LATITUDE + REAL_TYPE+ DEFAULT+ COMMA
                    +DBevent.KEY_TRANSPORT + TEXT_TYPE+ COMMA
                    +DBevent.KEY_CONTENT + TEXT_TYPE+ COMMA
                    +DBevent.KEY_SMARTREMIND + INT_TYPE+ DEFAULT+ COMMA
                    +DBevent.KEY_ALARMSTATUS + INT_TYPE+ DEFAULT+ COMMA
                    +DBevent.KEY_EDITTIME + INT_TYPE+ DEFAULT
                    +")";

    public DatabaseHandler(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DBevent.TABLE_NAME);
        sqLiteDatabase.execSQL(CREATE_EVENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Not required as at version 1
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}
