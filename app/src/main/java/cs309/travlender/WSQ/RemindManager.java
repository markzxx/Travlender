package cs309.travlender.WSQ;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import cs309.travlender.ZSQ.Event;
import cs309.travlender.ZXX.EventManager;
import cs309.travlender.ZXX.MainActivity;

import static cs309.travlender.WHL.TravelTimeService.startServiceTravelTime;

/**
 * Created by alicewu on 11/21/17.两边传消息的观察者。
 */

public class RemindManager {
    private EventManager eventManager;
    private Event event;
    private Context context;

    public RemindManager(EventManager e,Context context) {//需要可以被后台闹钟唤醒，重新获取所有事件
        this.context = context;
        eventManager = e;
        e.setRemindManager(this);


    }

    //not good design
    public void update(int eventID) {//notify one event with ID,
        Log.d("RemindManager", "update");

        event = eventManager.getEvent(eventID);
        if (!event.getLocation().equals("无") && event.getSmartRemind() == 1)
            startServiceTravelTime(context, event.getLatitude(), event.getLongitude(), "drive", eventID);

        else {
            // 启动服务，移到地图广播接收器里启动服务。
            Log.d("RemindManager", "启动");

            Intent updateIntent = new Intent(context, Alarm.class);
            updateIntent.setData(Uri.parse(String.valueOf(eventID)));
            updateIntent.putExtra("onwayTime", 0);
            context.startService(updateIntent);
            Log.d("RemindManager", "启动成功");
        }

    }

    public void updateAll() {


    }
}
