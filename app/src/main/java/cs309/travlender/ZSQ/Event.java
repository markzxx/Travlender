package cs309.travlender.ZSQ;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;


/**
 * Created by Administrator on 2017/10/16.
 */

public class Event implements ZHANGSHQIContract.EventInterface {

    private ContentValues value;

    public Event(){}

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
        return Integer.parseInt((String)value.get("event_id"));
    }

    public void setEventId(int id) {
        value.put("event_id",id);
    }

    public String getTitle() {
        return (String) value.get("title");
    }

    public void setTitle(String title) {
        value.put("title",title);
    }

    public long getStarttime() {
        return Long.valueOf((String) value.get("starttime"));
    }

    public void setStarttime(long starttime) {
        value.put("starttime",starttime);
    }

    public long getEndtime() {
        return Long.valueOf((String) value.get("endtime"));
    }

    public void setEndtime(long endtime) {
        value.put("endtime",endtime);

    }

    public long getAddtime() {
        return Long.valueOf((String)value.get("addtime"));
    }

    public void setAddtime(long addtime) {
        value.put("addtime",addtime);
    }
    public long getEdittime(){
        return Long.valueOf((String) value.get("edittime"));
    }
    public void setEdittime(long edittime){
        value.put("edittime",edittime);
    }
    public String getLocation(){
        return (String) value.get("location");
    }
    public void setLocation(String location){
        value.put("location",location);
    }
    public String getTransport(){
        return (String) value.get("transport");
    }
    public void setTransport(String transport){
        value.put("transport",transport);
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
