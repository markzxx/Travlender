package cs309.travlender.ZXX;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import cs309.travlender.ZSQ.Event;

/**
 * Created by Administrator on 2017/10/16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="Calendar";
    private static final String TABLE_NAME="event";
    private static final int VERSION=1;
    private static final String KEY_EVENT_ID="id";
    private static final String KEY_TITLE="title";
    private static final String KEY_START="starttime";
    private static final String KEY_END="endtime";

    //建表语句
    private static final String CREATE_TABLE="create table "+TABLE_NAME+"("+KEY_EVENT_ID+
            " integer primary key autoincrement,"+KEY_TITLE+" text not null,"+KEY_START+" text not null,"+
            KEY_END+" text not null);";
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addEvent(Event event){
        SQLiteDatabase db=this.getWritableDatabase();

        //使用ContentValues添加数据
        ContentValues values=new ContentValues();
        values.put(KEY_TITLE,event.getTitle());
        values.put(KEY_START,event.getStart());
        values.put(KEY_END,event.getEnd());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public Event getEvent(String name){
        SQLiteDatabase db=this.getWritableDatabase();

        //Cursor对象返回查询结果
        Cursor cursor=db.query(TABLE_NAME,new String[]{KEY_EVENT_ID,KEY_TITLE,KEY_START,KEY_END},
                KEY_TITLE+"=?",new String[]{name},null,null,null,null);


        Event event=null;
        //注意返回结果有可能为空
        if(cursor.moveToFirst()){
            event=new Event(cursor.getInt(0),cursor.getString(1), cursor.getString(2),cursor.getString(3));
        }
        return event;
    }
    public int getEventCounts(){
        String selectQuery="SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);
        cursor.close();

        return cursor.getCount();
    }

    //查找所有Event
    public List<Event> getALllEvent(){
        List<Event> EventList=new ArrayList<Event>();

        String selectQuery="SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                Event event=new Event();
                event.setId(Integer.parseInt(cursor.getString(0)));
                event.setTitle(cursor.getString(1));
                event.setStart(cursor.getString(2));
                event.setEnd(cursor.getString(3));
                EventList.add(event);
            }while(cursor.moveToNext());
        }
        return EventList;
    }

    //更新Event
    public int updateEvent(Event event){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_TITLE,event.getTitle());
        values.put(KEY_START,event.getStart());
        values.put(KEY_END,event.getEnd());

        return db.update(TABLE_NAME,values,KEY_EVENT_ID+"=?",new String[]{String.valueOf(event.getId())});
    }
    public void deleteEvent(Event event){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,KEY_EVENT_ID+"=?",new String[]{String.valueOf(event.getId())});
        db.close();
    }
}
