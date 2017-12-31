package cs309.travlender.Tools;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;

import cs309.travlender.MainActivity.ZHANGSHQIContract;


/**
 * Created by Administrator on 2017/10/16.
 */

public class Event implements ZHANGSHQIContract.EventInterface {

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
        return value.getAsInteger(DB._ID);
    }

    public void setEventId(int id) {
        value.put(DB._ID,id);
    }

    public String getTitle() {
        return value.getAsString(DB.KEY_TITLE);
    }

    public void setTitle(String title) {
        value.put(DB.KEY_TITLE,title);
    }

    public long getStarttime() {
        return value.getAsLong("starttime");
    }

    public void setStarttime(long starttime) {
        value.put("starttime",starttime);
    }

    public long getEndtime() {
        return value.getAsLong("endtime");
    }

    public void setEndtime(long endtime) {
        value.put("endtime",endtime);

    }

    public long getAddtime() {
        return value.getAsLong("addtime");
    }
    public void setAddtime(long addtime) {
        value.put("addtime",addtime);
    }
    public long getEdittime(){
        return value.getAsLong("edittime");
    }
    public void setEdittime(long edittime){
        value.put("edittime",edittime);
    }
    public String getLocation(){
        return value.getAsString("location");
    }
    public void setLocation(String location){
        value.put("location",location);
    }
    public String getTransport(){
        return value.getAsString("transport");
    }
    public void setTransport(String transport){
        value.put("transport",transport);
    }
    public int getEarlytime(){ return value.getAsInteger("remindtime");}
    public void setEarlytime(String remindtime){ value.put(DB.KEY_REMINDTIME,remindtime);}
    public double getLongitude(){
        return value.getAsDouble("longitude");
    }
    public void setLongitude(double longitude){
        value.put(DB.KEY_LONGITUDE,longitude);
    }
    public double getLatitude(){
        return value.getAsDouble("latitude");
    }
    public void setLatitude(double latitude){
        value.put(DB.KEY_LATITUDE,latitude);
    }
    public String  getContent(){
        return value.getAsString("content");
    }
    public void setContent(String content){
        value.put(DB.KEY_CONTENT,content);
    }
    public int getSmartRemind(){
        return value.getAsInteger(DB.KEY_SMARTREMIND);
    }
    public void setSmartRemind(int smartRemind){
        value.put(DB.KEY_SMARTREMIND,smartRemind);
    }
    public int getAlarmStatus(){
        return value.getAsInteger(DB.KEY_ALARMSTATUS);
    }
    public void setAlarmStatus(int alarmcode){
        value.put(DB.KEY_ALARMSTATUS, getAlarmStatus()|alarmcode);
        EventManager.getInstence().editEvent(this);
    }
    public boolean isTravelAlarm(){
        return getSmartRemind()==1 && (getAlarmStatus()&TRAVELCODE)==0;
    }
    public boolean isEarlyAlarm(){
        return getEarlytime()!=0 && (getAlarmStatus()&EARLYCODE)==0;
    }
    public boolean isCommomAlarm(){
        return (getAlarmStatus()&COMMOMCODE)==0;
    }
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel out, int flags) {
//        out.writeString(title);
//    }
//    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>(){
//        public Event createFromParcel(Parcel in)
//        {
//            return new Event(in);
//        }
//
//        public Event[] newArray(int size)
//        {
//            return new Event[size];
//        }
//    };
//    public Event(Parcel in){
//        title = in.readString();
//    }
}
