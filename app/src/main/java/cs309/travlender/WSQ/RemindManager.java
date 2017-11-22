package cs309.travlender.WSQ;

import android.app.Activity;
import android.content.Intent;

import android.net.Uri;

import cs309.travlender.ZXX.EventManager;

/**
 * Created by alicewu on 11/21/17.两边传消息的观察者。
 */

public class RemindManager extends Activity {
    private EventManager eventManager;

    public RemindManager(EventManager e){//需要可以被后台闹钟唤醒，重新获取所有事件
        eventManager = e;
        e.setRemindManager(this);


    }
    public void update(int eventID){//notify one event with ID, 启动服务
        Intent updateIntent = new Intent(this, Alarm.class);
        updateIntent.setData(Uri.parse(String.valueOf(eventID)));
        startService(updateIntent);
    }
    public void updateAll(){


    }

}
