package cs309.travlender.ZSQ;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;
import java.util.Map;


/**
 * Created by Administrator on 2017/10/16.
 */

public class Event implements Serializable {
    private int id;
    private String title;
    private String addtime;
    private String start;
    private String end;
    private ContentValues value;
    private Map<String,Object> map;


    public Event(){}

    public Event(String title,String addtime, String start,String end) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.addtime = addtime;
        value = new ContentValues();
//        value.put("addtime",addtime);
        value.put("title",title);
        value.put("starttime",start);
        value.put("endtime",end);
//        map = new HashMap<>();
//        map.put("title",title);
//        map.put("start",start);
//        map.put("end",end);
    }

    public Event(Cursor cursor){
        this.id = cursor.getInt(0);
        this.title = cursor.getString(1);
        this.addtime = cursor.getString(2);
        this.start = cursor.getString(3);
        this.end = cursor.getString(4);
        value = new ContentValues();
        value.put("title",title);
        value.put("starttime",start);
        value.put("endtime",end);
//        value.put("addtime",addtime);
    }

    public ContentValues getValue(){
        return this.value;
    }
    public void setValue(ContentValues value) {
        this.value = value;
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
