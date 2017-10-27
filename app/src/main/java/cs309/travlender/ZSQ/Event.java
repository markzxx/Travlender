package cs309.travlender.ZSQ;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Map;

import cs309.travlender.ZSQ.ZHANGSHQIContract;


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
        String[] names = cursor.getColumnNames();
        for(int i = 0;i<names.length;i++){
            if(names[i].equals("event_id"))
                value.put(names[i],cursor.getInt(i));
            else
                value.put(names[i],cursor.getString(i));
        }
    }

    public ContentValues getValue(){
        return this.value;
    }

    public void setValue(ContentValues value) {
        this.value = value;
    }

    public int getEventId() {
        return (int)value.get("event_id");
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

    public String getStarttime() {
        return (String) value.get("starttime");
    }

    public void setStarttime(String starttime) {
        value.put("starttime",starttime);
    }

    public String getEndtime() {
        return (String) value.get("endtime");
    }

    public void setEndtime(String endtime) {
        value.put("endtime",endtime);

    }

    public String getAddtime() {
        return (String) value.get("addtime");
    }

    public void setAddtime(String addtime) {
        value.put("addtime",addtime);
    }
    public String getEdittime(){
        return (String) value.get("edittime");
    }
    public void setEdittime(String edittime){
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
