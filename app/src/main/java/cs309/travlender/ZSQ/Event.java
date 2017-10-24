package cs309.travlender.ZSQ;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2017/10/16.
 */

public class Event implements Serializable {
    private int id;
    private String title;
    private String start;
    private String end;
    private ContentValues info;
    private Map<String,Object> map;


    public Event(){}

    public Event(int id, String title, String start,String end) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.end = end;
        info = new ContentValues();
        info.put("id",id);
        info.put("title",title);
        info.put("start",start);
        info.put("end",end);
        map = new HashMap<>();
        map.put("id",id);
        map.put("title",title);
        map.put("start",start);
        map.put("end",end);
    }

    public Event(Cursor cursor){
        this.id = Integer.parseInt(cursor.getString(0));
        this.title = cursor.getString(1);
        this.start = cursor.getString(2);
        this.end = cursor.getString(3);


    }

    public ContentValues getInfo(){
        return this.info;
    }
    public void setInfo(ContentValues info) {
        this.info = info;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
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

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }


}
