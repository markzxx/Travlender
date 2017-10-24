package cs309.travlender.ZXX;

import android.provider.BaseColumns;

/**
 * Created by markz on 2017-10-24.
 */

public class DatabaseContract {
    public static final String DATABASE_NAME="Travlender";
    public static final int VERSION=1;
    private DatabaseContract(){}

    public static class DBevent implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String _ID = "event_id";
        public static final String KEY_TITLE = "title";
        //public static final String KEY_ADDTIME = "addtime";
        private static final String KEY_START="starttime";
        private static final String KEY_END="endtime";
    }

    public static class DBpreference implements BaseColumns {
        public static final String TABLE_NAME = "preference";
        public static final String _ID = "preference_id";
        public static final String KEY_TRANSPORT = "transport";
        public static final String KEY_REMINDTYPE = "remindtype";
    }
}
