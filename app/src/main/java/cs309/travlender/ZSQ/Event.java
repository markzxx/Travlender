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
    private int id;
    private String title;
    private String addtime;
    private String starttime;
    private String endtime;
    private ContentValues value;

    public Event(){}

    public Event(ContentValues value) {


        this.value = value;
        this.title = value.get("title");
        this.starttime = value.get("starttime");
        this.endtime = value.get("endtime");
        this.addtime = value.get("addtime");
    }

    public Event(Cursor cursor){
        this.id = cursor.getInt(0);
        this.title = cursor.getString(1);
        this.addtime = cursor.getString(2);
        this.starttime = cursor.getString(3);
        this.endtime = cursor.getString(4);
        value = new ContentValues();
        value.put("title",title);
        value.put("starttime",starttime);
        value.put("endtime",endtime);
        value.put("addtime",addtime);
    }

    public ContentValues getValue(){
        return this.value;
    }

    public void setValue(ContentValues value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getstarttime() {
        return starttime;
    }

    public void setstarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getendtime() {
        return endtime;
    }

    public void setendtime(String endtime) {
        this.endtime = endtime;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
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
