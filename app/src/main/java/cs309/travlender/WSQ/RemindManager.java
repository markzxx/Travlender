package cs309.travlender.WSQ;

import cs309.travlender.ZXX.EventManager;

/**
 * Created by alicewu on 11/21/17.
 */

public class RemindManager {
    private EventManager eventManager;

    public RemindManager(EventManager e){
        eventManager = e;
        e.setRemindManager(this);
    }


}
