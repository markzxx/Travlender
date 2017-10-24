package cs309.travlender.ZXX;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import cs309.travlender.ZSQ.Event;

/**
 * Created by markz on 2017-10-24.
 */

public class EventManager implements EventManagerContract.Manager {
    DatabaseHandler dbHelper;
    Event event;
    EventManager(@NonNull Context context){
        dbHelper = new DatabaseHandler(context);
    }
    public void addEvent(Event event) {
        this.event = event;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values=event.getInfo();
        db.insert(DatabaseContract.DBevent.TABLE_NAME, null, values);
        db.close();
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
        ContentValues values=event.getInfo();
        String table_name = DatabaseContract.DBevent.TABLE_NAME;
        db.update(table_name,values,null,null);
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
}
