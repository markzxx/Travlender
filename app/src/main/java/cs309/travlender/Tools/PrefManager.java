package cs309.travlender.Tools;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by alicewu on 1/3/18.
 */

public class PrefManager {
    private static PrefManager instence;
    public static Preference pref;
    private static final String pref_id = DatabaseContract.DBpreference.UNIQUE_ID;
    public DatabaseHandler dbHelper;

    private PrefManager() {
        dbHelper = new DatabaseHandler(MyContext.getContext());
    }

    public static PrefManager getInstence() {
        if (instence == null) {
            instence = new PrefManager();
        }
        return instence;
    }

    public void editPref(Preference pref) {
        this.pref = pref;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = pref.getValue();
        String table_name = DatabaseContract.DBpreference.TABLE_NAME;
        String where = DatabaseContract.DBpreference._ID + " = " + pref_id;
        db.update(table_name, values, where, null);
        db.close();

    }
}
