package cs309.travlender.Tools;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;

/**
 * Created by alicewu on 1/3/18.
 */

public class Preferences {
    private ContentValues value;

    private static DatabaseContract.DBpreference DB_pref = new DatabaseContract.DBpreference();
    public Preferences(){
        value = new ContentValues();
    }
    public Preferences(Cursor cursor){
        value = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor,value);
    }

    public ContentValues getValue() {
        return this.value;
    }

    public void setValue(ContentValues value) {
        this.value = value;
    }

    public String getTransport() {
        return value.getAsString(DatabaseContract.DBpreference.KEY_TRANSPORT);
    }

    public void setTransport(String transport) {
        value.put(DatabaseContract.DBpreference.KEY_TRANSPORT, transport);
    }

    public int getIsVibrate() {
        return value.getAsInteger(DatabaseContract.DBpreference.KEY_ISVIBRATE);
    }

    public void setIsVibrate(int isVibrate) {
        value.put(DatabaseContract.DBpreference.KEY_ISVIBRATE, isVibrate);
    }

    public String getRingtone() {
        return value.getAsString(DatabaseContract.DBpreference.KEY_RINGTONE);
    }

    public void setRingtone(String ringtone) {
        value.put(DatabaseContract.DBpreference.KEY_RINGTONE, ringtone);
    }

    public int getIsAutoplan() {
        return value.getAsInteger(DatabaseContract.DBpreference.KEY_ISAUTOPLAN);
    }

    public void setIsAutoplan(int isAutoplan) {
        value.put(DatabaseContract.DBpreference.KEY_ISAUTOPLAN, isAutoplan);
    }

    public int getIsPopwin() {
        return value.getAsInteger(DatabaseContract.DBpreference.KEY_ISPOPWIN);
    }

    public void setIsPopwin(int isPopwin) {
        value.put(DatabaseContract.DBpreference.KEY_ISPOPWIN, isPopwin);
    }

    public int getRemindBefore() {
        return value.getAsInteger(DatabaseContract.DBpreference.KEY_REMINDBEFORE);
    }

    public void setRemindBefore(int remindBefore) {
        value.put(DatabaseContract.DBpreference.KEY_REMINDBEFORE, remindBefore);
    }

}
