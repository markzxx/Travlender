package cs309.travlender.WSQ;

import cs309.travlender.ZXX.EventManager;

/**
 * Created by alicewu on 11/21/17.两边传消息的观察者。
 */

public class RemindManager {
    private EventManager eventManager;

    public RemindManager(EventManager e){//需要可以被后台闹钟唤醒，重新获取所有事件
        eventManager = e;
        e.setRemindManager(this);

    }
    public void update(int eventID){//notify one event with ID

    }
    public void updateAll(){

    }

}
