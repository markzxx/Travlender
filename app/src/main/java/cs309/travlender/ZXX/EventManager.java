package cs309.travlender.ZXX;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import cs309.travlender.ZSQ.Event;

/**
 * Created by markz on 2017-10-24.
 */

public class EventManager implements EventManagerContract.Manager {
    DatabaseHandler db;
    EventManager(@NonNull Context context){
        db = new DatabaseHandler(context);
    }
    public void addEvent(Event event) {

    }

    public void deleteEvent(int id) {

    }

    public void editEvent(Event event) {

    }

    public void openEvent(int id) {

    }


}
