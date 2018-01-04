package cs309.travlender.Tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cs309.travelender.R;


/**
 * Created by Administrator on 2017/10/16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    public static DatabaseContract.DBevent DBevent = new DatabaseContract.DBevent();
    public static DatabaseContract.DBpreference DBpreference = new DatabaseContract.DBpreference();
    private static final String COMMA = ",";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String DEFAULT = " DEFAULT 0";
    //create tables
    private static final String CREATE_EVENT =
            "create table IF NOT EXISTS " + DatabaseContract.DBevent.TABLE_NAME + " ("
                    + DatabaseContract.DBevent._ID + INT_TYPE + " primary key autoincrement,"
                    + DatabaseContract.DBevent.KEY_TITLE + TEXT_TYPE + COMMA
                    + DatabaseContract.DBevent.KEY_ADDTIME + INT_TYPE + COMMA
                    + DatabaseContract.DBevent.KEY_STARTTIME + INT_TYPE + COMMA
                    + DatabaseContract.DBevent.KEY_ENDTIME + INT_TYPE + COMMA
                    + DatabaseContract.DBevent.KEY_REMINDTIME + INT_TYPE + COMMA
                    + DatabaseContract.DBevent.KEY_LOCATION + TEXT_TYPE + DEFAULT + COMMA
                    + DatabaseContract.DBevent.KEY_LONGITUDE + REAL_TYPE + DEFAULT + COMMA
                    + DatabaseContract.DBevent.KEY_LATITUDE + REAL_TYPE + DEFAULT + COMMA
                    + DatabaseContract.DBevent.KEY_TRANSPORT + TEXT_TYPE + COMMA
                    + DatabaseContract.DBevent.KEY_CONTENT + TEXT_TYPE + COMMA
                    + DatabaseContract.DBevent.KEY_SMARTREMIND + INT_TYPE + DEFAULT + COMMA
                    + DatabaseContract.DBevent.KEY_ALARMSTATUS + INT_TYPE + DEFAULT + COMMA
                    + DatabaseContract.DBevent.KEY_EDITTIME + INT_TYPE + DEFAULT
                    + ")";

    private static final String CREATE_PREF =
            "create table IF NOT EXISTS " + DatabaseContract.DBpreference.TABLE_NAME + " ("
                    + DatabaseContract.DBpreference._ID + INT_TYPE + " primary key" + COMMA
                    + DatabaseContract.DBpreference.KEY_REMINDBEFORE + INT_TYPE+COMMA
                    + DatabaseContract.DBpreference.KEY_TRANSPORT + TEXT_TYPE+COMMA
                    + DatabaseContract.DBpreference.KEY_ISAUTOPLAN + INT_TYPE+COMMA
                    + DatabaseContract.DBpreference.KEY_ISPOPWIN + INT_TYPE+COMMA
                    + DatabaseContract.DBpreference.KEY_ISVIBRATE + INT_TYPE+COMMA
                    + DatabaseContract.DBpreference.KEY_RINGTONE + TEXT_TYPE + ")";
    //插入一行设置默认值
    private static final String INSERT_PREF =
            "insert or ignore into " + DatabaseContract.DBpreference.TABLE_NAME + "("
                    + DatabaseContract.DBpreference._ID + COMMA
                    + DatabaseContract.DBpreference.KEY_REMINDBEFORE + COMMA + DatabaseContract.DBpreference.KEY_TRANSPORT + COMMA
                    + DatabaseContract.DBpreference.KEY_ISAUTOPLAN + COMMA + DatabaseContract.DBpreference.KEY_ISPOPWIN + COMMA
                    + DatabaseContract.DBpreference.KEY_ISVIBRATE + COMMA + DatabaseContract.DBpreference.KEY_RINGTONE + ") values ("
                    + DatabaseContract.DBpreference.UNIQUE_ID + COMMA
                    + MyContext.getContext().getString(R.string.pref_default_remind_before) + COMMA
                    + "'" + MyContext.getContext().getString(R.string.pref_default_transportation) + "'" + COMMA
                    + DatabaseContract.DBpreference.boolToInt(MyContext.getContext().getString(R.string.pref_default_auto_plan)) + COMMA
                    + DatabaseContract.DBpreference.boolToInt(MyContext.getContext().getString(R.string.pref_default_pop_win)) + COMMA
                    + DatabaseContract.DBpreference.boolToInt(MyContext.getContext().getString(R.string.pref_default_vibrate)) + COMMA
                    + "'" + MyContext.getContext().getString(R.string.pref_default_ringtone) + "'" + ")";

    public DatabaseHandler(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DBevent.TABLE_NAME);
        sqLiteDatabase.execSQL(CREATE_EVENT);
        sqLiteDatabase.execSQL(CREATE_PREF);
        sqLiteDatabase.execSQL(INSERT_PREF);
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
