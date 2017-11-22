package cs309.travlender.WSQ;

import android.app.Activity;
import android.content.Intent;

import android.net.Uri;

import cs309.travlender.ZSQ.Event;
import cs309.travlender.ZXX.EventManager;

import static cs309.travlender.WHL.TravelTimeService.startServiceTravelTime;

/**
 * Created by alicewu on 11/21/17.两边传消息的观察者。
 */

public class RemindManager extends Activity {
    private EventManager eventManager;
    private Event event;

    public RemindManager(EventManager e){//需要可以被后台闹钟唤醒，重新获取所有事件
        eventManager = e;
        e.setRemindManager(this);


    }
    //not good design
    public void update(int eventID){//notify one event with ID,
        event = eventManager.getEvent(eventID);
        if(event.getLocation() != "无" && event.getSmartRemind()==1)
            startServiceTravelTime(this, event.getLatitude(),event.getLongitude(),event.getTransport(),eventID);

        else{
        // 启动服务，移到地图广播接收器里启动服务。
        Intent updateIntent = new Intent(this, Alarm.class);
        updateIntent.setData(Uri.parse(String.valueOf(eventID)));
            updateIntent.putExtra("onwayTime", 0);
        startService(updateIntent);}

    }
    public void updateAll(){


    }

}
