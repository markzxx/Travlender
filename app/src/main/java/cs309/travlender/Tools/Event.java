package cs309.travlender.Tools;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;


/**
 * Created by Administrator on 2017/10/16.
 */

public class Event{

    public static final int TRAVELCODE = 4; //binary 100
    public static final int EARLYCODE = 2; //binary 010
    public static final int COMMOMCODE = 1; //binary 001

    private ContentValues value;

    private static DatabaseContract.DBevent DB = new DatabaseContract.DBevent();

    public Event(){
        value = new ContentValues();
    }

    public Event(ContentValues value) {
        this.value = value;
    }

    public Event(Cursor cursor){
        value = new ContentValues();
        DatabaseUtils.cursorRowToContentValues(cursor,value);
    }

    public ContentValues getValue(){
        return this.value;
    }

    public void setValue(ContentValues value) {
        this.value = value;
    }

    public int getEventId() {
        return value.getAsInteger(DatabaseContract.DBevent._ID);
    }

    public void setEventId(int id) {
        value.put(DatabaseContract.DBevent._ID,id);
    }

    public String getTitle() {
        return value.getAsString(DatabaseContract.DBevent.KEY_TITLE);
    }

    public void setTitle(String title) {
        value.put(DatabaseContract.DBevent.KEY_TITLE,title);
    }

    public long getStarttime() {
        return value.getAsLong(DatabaseContract.DBevent.KEY_STARTTIME);
    }

    public void setStarttime(long starttime) {
        value.put(DatabaseContract.DBevent.KEY_STARTTIME,starttime);
    }

    public long getEndtime() {
        return value.getAsLong(DatabaseContract.DBevent.KEY_ENDTIME);
    }

    public void setEndtime(long endtime) {
        value.put("endtime",endtime);

    }

    public long getAddtime() {
        return value.getAsLong(DatabaseContract.DBevent.KEY_ADDTIME);
    }
    public void setAddtime(long addtime) {
        value.put(DatabaseContract.DBevent.KEY_ADDTIME,addtime);
    }
    public long getEdittime(){
        return value.getAsLong(DatabaseContract.DBevent.KEY_EDITTIME);
    }
    public void setEdittime(long edittime){
        value.put(DatabaseContract.DBevent.KEY_EDITTIME,edittime);
    }
    public String getLocation(){
        return value.getAsString(DatabaseContract.DBevent.KEY_LOCATION);
    }
    public void setLocation(String location){
        value.put(DatabaseContract.DBevent.KEY_LOCATION,location);
    }
    public String getTransport(){
        return value.getAsString(DatabaseContract.DBevent.KEY_TRANSPORT);
    }
    public void setTransport(String transport){
        value.put(DatabaseContract.DBevent.KEY_TRANSPORT,transport);
    }
    public int getEarlytime(){ return value.getAsInteger(DatabaseContract.DBevent.KEY_REMINDTIME);}
    public void setEarlytime(String remindtime){ value.put(DatabaseContract.DBevent.KEY_REMINDTIME,remindtime);}
    public double getLongitude(){
        return value.getAsDouble(DatabaseContract.DBevent.KEY_LONGITUDE);
    }
    public void setLongitude(double longitude){
        value.put(DatabaseContract.DBevent.KEY_LONGITUDE,longitude);
    }
    public double getLatitude(){
        return value.getAsDouble(DatabaseContract.DBevent.KEY_LATITUDE);
    }
    public void setLatitude(double latitude){
        value.put(DatabaseContract.DBevent.KEY_LATITUDE,latitude);
    }
    public String  getContent(){
        return value.getAsString(DatabaseContract.DBevent.KEY_CONTENT);
    }
    public void setContent(String content){
        value.put(DatabaseContract.DBevent.KEY_CONTENT,content);
    }
    public int getSmartRemind(){
        return value.getAsInteger(DatabaseContract.DBevent.KEY_SMARTREMIND);
    }
    public void setSmartRemind(int smartRemind){
        value.put(DatabaseContract.DBevent.KEY_SMARTREMIND,smartRemind);
    }
    public int getAlarmStatus(){
        return value.getAsInteger(DatabaseContract.DBevent.KEY_ALARMSTATUS);
    }
    public void setAlarmStatus(int alarmcode){
        value.put(DatabaseContract.DBevent.KEY_ALARMSTATUS, getAlarmStatus()|alarmcode);
        EventManager.getInstence().editEvent(this);
    }
    public boolean isTravelAlarm(){
        return getSmartRemind()==1 && (getAlarmStatus()&TRAVELCODE)==0 && !getLocation().equals("1");
    }
    public boolean isEarlyAlarm(){
        return getEarlytime()!=0 && (getAlarmStatus()&EARLYCODE)==0;
    }
    public boolean isCommomAlarm(){
        return (getAlarmStatus()&COMMOMCODE)==0;
    }
}
