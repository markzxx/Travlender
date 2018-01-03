package cs309.travlender.Tools;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cs309.travlender.Remainder.RemindService;

/**
 * Created by markz on 2017-10-24.
 */

public class EventManager implements EventManagerContract.Manager {
    private static EventManager instence;
    public DatabaseHandler dbHelper;
    public static Event event;
    public static List<Event> SearchList;
//    private static RemindManager RM;
    private EventManager(){
        dbHelper = new DatabaseHandler(MyContext.getContext());
    }

    public static EventManager getInstence(){
        if(instence == null){
            instence = new EventManager();
        }
        return instence;
    }

    public void update(){
        Intent i = new Intent(MyContext.getContext(), RemindService.class);
        i.putExtra(RemindService.TYPE, RemindService.TYPE_INIT);
        MyContext.getContext().startService(i);
    }

//    public void setRemindManager(RemindManager rm){
//        RM = rm;
//    }
//
//    public RemindManager getRemindManager(){
//        return RM;
//    }

    public int addEvent(Event event) {
        this.event = event;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values=event.getValue();
        long id = db.insert(DatabaseContract.DBevent.TABLE_NAME, null, values);
        db.close();
        update();
        return (int)id;
    }

    public void deleteEvent(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String table_name = DatabaseContract.DBevent.TABLE_NAME;
        String where = DatabaseContract.DBevent._ID + " = ? ";
        String[] whereArgs = {String.valueOf(id)};
        db.delete(table_name,where,whereArgs);
        db.close();
        update();
    }

    public void editEvent(Event event) {
        this.event = event;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values=event.getValue();
        String where = DatabaseContract.DBevent._ID+" = " + event.getEventId();
        String table_name = DatabaseContract.DBevent.TABLE_NAME;
        db.update(table_name,values,where,null);
        db.close();
        update();
    }

    public Event getEvent(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String table_name = DatabaseContract.DBevent.TABLE_NAME;
        String[] selection = {"*"};
        String where =DatabaseContract.DBevent._ID + " = ? ";
        String[] whereArgs = {String.valueOf(id)};
        Cursor cursor = db.query(table_name,selection,where,whereArgs,null,null,null);
        cursor.moveToFirst();
        this.event = new Event(cursor);
        db.close();
        return event;
    }

    public List<Event> searchEvent(String title){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Event> list = new ArrayList<>();

        String table_name = DatabaseContract.DBevent.TABLE_NAME;
        String[] selection = {"*"};
        String where = DatabaseContract.DBevent.KEY_TITLE + " like ? ";
        String[] whereArgs = {"%"+title+"%"};
        String order = DatabaseContract.DBevent.KEY_ADDTIME + " DESC";

        Cursor cursor = db.query(table_name,selection,where,whereArgs,null,null,order);
        if(cursor.moveToFirst()){
            do{
                list.add(new Event(cursor));
            }while(cursor.moveToNext());
        }
        SearchList = list;
        db.close();
        return list;
    }

    public void deleteAllEvent() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String table_name = DatabaseContract.DBevent.TABLE_NAME;
        db.delete(table_name,null,null);
        db.close();
        update();
    }

    public List<Event> getAllEvent(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Event> list = new ArrayList<>();
        String table_name = DatabaseContract.DBevent.TABLE_NAME;
        String[] selection = {"*"};
        String order = DatabaseContract.DBevent.KEY_STARTTIME;
        Cursor cursor = db.query(table_name,selection,null,null,null,null,order);
        if(cursor.moveToFirst()){
            do{
                list.add(new Event(cursor));
            }while(cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public List<Event> getEvents_aDay(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long starttime = new Date().getTime();
        long endtime = starttime + 86400000;
        List<Event> list = new ArrayList<>();
        String table_name = DatabaseContract.DBevent.TABLE_NAME;
        String[] selection = {"*"};
        String where = "(starttime>? and starttime<?)";
        String[] whereArgs ={starttime+"",endtime+""};

        String order = DatabaseContract.DBevent.KEY_STARTTIME;
        Cursor cursor = db.query(table_name,selection,where,whereArgs,null,null,order);
        if(cursor.moveToFirst()){
            do{
                list.add(new Event(cursor));
            }while(cursor.moveToNext());
        }
        SearchList = list;
        db.close();
        return list;
    }

    public List<Event> getEvents(long starttime,long endtime) // timestamps
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Event> list = new ArrayList<>();
        String table_name = DatabaseContract.DBevent.TABLE_NAME;
        String[] selection = {"*"};
        String where = "(starttime>? and starttime<?) or (endtime>? and endtime<?)";
        String[] whereArgs ={starttime+"",endtime+"",starttime+"",endtime+""};
        String order = DatabaseContract.DBevent.KEY_STARTTIME;
        Cursor cursor = db.query(table_name,selection,where,whereArgs,null,null,order);
        if(cursor.moveToFirst()){
            do{
                list.add(new Event(cursor));
            }while(cursor.moveToNext());
        }
        SearchList = list;
        db.close();
        return list;
    }

//    public List<Event> getEvents(String starttime,String endtime) // format like yyyy-mm-dd HH:MM:SS
//    {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        List<Event> list = new ArrayList<>();
//        String table_name = DatabaseContract.DBevent.TABLE_NAME;
//        String[] selection = {"*"};
//        Timestamp ts = new Timestamp(System.currentTimeMillis());
//        String where = "(starttime>? and starttime<?) or (endtime>? and endtime<?)";
//        String[] whereArgs ={ts.valueOf(starttime).getTime()+"",ts.valueOf(endtime.toString()).getTime()+"",ts.valueOf(starttime).getTime()+"",ts.valueOf(endtime.toString()).getTime()+""};
//        String order = DatabaseContract.DBevent.KEY_STARTTIME;
//        Cursor cursor = db.query(table_name,selection,where,whereArgs,null,null,order);
//        if(cursor.moveToFirst()){
//            do{
//                list.add(new Event(cursor));
//            }while(cursor.moveToNext());
//        }
//        SearchList = list;
//        db.close();
//        return list;
//    }

    public List<Event> getEvents(String starttime,String endtime){
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        long start = ts.valueOf(starttime).getTime();
        long end = ts.valueOf(endtime.toString()).getTime();
        return getEvents(start,end);

    }

    public List<Event> getEvents(String date){
        String start = date+" 00:00:00";
        String end = date+" 23:59:59";
        return getEvents(start,end);
    }
}
