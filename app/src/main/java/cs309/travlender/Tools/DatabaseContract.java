package cs309.travlender.Tools;

import android.provider.BaseColumns;

/**
 * Created by markz on 2017-10-24.
 */

public class DatabaseContract {
    public static final String DATABASE_NAME="Travlender";
    public static final int VERSION=1;
    private DatabaseContract(){}

    public static class DBevent implements BaseColumns {
        public static final String TABLE_NAME = "event";
        public static final String _ID = "_id";
        public static final String KEY_TITLE = "title";
        public static final String KEY_ADDTIME = "addtime";
        public static final String KEY_STARTTIME="starttime";
        public static final String KEY_ENDTIME="endtime";
        public static final String KEY_LOCATION="location";
        public static final String KEY_LONGITUDE="longitude";
        public static final String KEY_LATITUDE="latitude";
        public static final String KEY_TRANSPORT="transport";
        public static final String KEY_EDITTIME = "edittime";
        public static final String KEY_REMINDTIME = "remindtime";
        public static final String KEY_CONTENT = "content";
        public static final String KEY_SMARTREMIND = "smartremind";
        public static final String KEY_ALARMSTATUS = "alarmstatus";
    }

    public static class DBpreference implements BaseColumns {
        public static final String TABLE_NAME = "preference";
        public static final String _ID = "preferenceid";
        public static final String KEY_TRANSPORT = "transport";
        public static final String KEY_REMINDTYPE = "remindtype";
    }
}
