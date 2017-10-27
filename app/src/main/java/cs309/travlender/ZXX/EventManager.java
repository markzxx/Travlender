package cs309.travlender.ZXX;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cs309.travlender.ZSQ.Event;

import static android.R.attr.id;

/**
 * Created by markz on 2017-10-24.
 */

public class EventManager implements EventManagerContract.Manager {
    public DatabaseHandler dbHelper;
    static Event event;
    static List<Event> SearchList;
    public EventManager(@NonNull Context context){
        dbHelper = new DatabaseHandler(context);
    }
    public int addEvent(Event event) {
        this.event = event;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values=event.getValue();
        long id = db.insert(DatabaseContract.DBevent.TABLE_NAME, null, values);
        db.close();
        return (int)id;
    }

    public void deleteEvent(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String table_name = DatabaseContract.DBevent.TABLE_NAME;
        String where = DatabaseContract.DBevent._ID + " = ? ";
        String[] whereArgs = {String.valueOf(id)};
        db.delete(table_name,where,whereArgs);
        db.close();
    }

    public void editEvent(Event event) {
        this.event = event;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values=event.getValue();
        String where = DatabaseContract.DBevent._ID+" = " + event.getId();
        String table_name = DatabaseContract.DBevent.TABLE_NAME;
        db.update(table_name,values,where,null);
        db.close();
    }

    public Event openEvent(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String table_name = DatabaseContract.DBevent.TABLE_NAME;
        String[] selection = {"*"};
        String where = DatabaseContract.DBevent._ID + " = ? ";
        String[] whereArgs = {String.valueOf(id)};
        Cursor cursor = db.query(table_name,selection,where,whereArgs,null,null,null);
        this.event = new Event(cursor);
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
        return list;
    }

    public void deleteAllEvent() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String table_name = DatabaseContract.DBevent.TABLE_NAME;
        String where = DatabaseContract.DBevent._ID + " = ? ";
        String[] whereArgs = {String.valueOf(id)};
        db.delete(table_name,null,null);
        db.close();
    }

    public List<Event> getAllEvent(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Event> list = new ArrayList<>();
        String table_name = DatabaseContract.DBevent.TABLE_NAME;
        String[] selection = {"*"};
        Cursor cursor = db.query(table_name,selection,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                list.add(new Event(cursor));
            }while(cursor.moveToNext());
        }
        return list;
    }
}
