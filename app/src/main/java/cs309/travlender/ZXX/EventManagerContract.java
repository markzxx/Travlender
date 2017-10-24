package cs309.travlender.ZXX;

import cs309.travlender.ZSQ.Event;

/**
 * Created by markz on 2017-10-24.
 */

public interface EventManagerContract {
    interface Manager {

        void addEvent(Event event);

        void deleteEvent(int id);

        void editEvent(Event event);

        void openEvent(int id);
    }
}
