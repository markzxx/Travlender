package cs309.travlender.Tools;

import java.util.List;

/**
 * Created by markz on 2017-10-24.
 */

public interface EventManagerContract {
    interface Manager {

        int addEvent(Event event);

        void deleteEvent(int id);

        void editEvent(Event event);

        Event getEvent(int id);

        List<Event> getAllEvent();

        void deleteAllEvent();

        List<Event> searchEvent(String name);

        List<Event> getEvents_aDay(); // get events from now to next day the same time

        List<Event> getEvents(long starttime,long endtime); // timestamps

        List<Event> getEvents(String starttime,String endtime); // format like yyyy-mm-dd HH:MM:SS
    }
}
