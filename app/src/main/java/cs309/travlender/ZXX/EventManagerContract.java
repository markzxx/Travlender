package cs309.travlender.ZXX;

import java.util.List;

import cs309.travlender.ZSQ.Event;

/**
 * Created by markz on 2017-10-24.
 */

public interface EventManagerContract {
    interface Manager {

        int addEvent(Event event);

        void deleteEvent(int id);

        void editEvent(Event event);

        Event openEvent(int id);

        List<Event> getAllEvent();

        void deleteAllEvent();

        List<Event> searchEvent(String name);

        List<Event> searchEvents(long starttime,long endtime); // timestamps

        List<Event> searchEvents(String starttime,String endtime); // format like yyyy-mm-dd HH:MM:SS
    }
}
