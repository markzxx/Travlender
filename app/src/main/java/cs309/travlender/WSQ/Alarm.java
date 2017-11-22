package cs309.travlender.WSQ;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

import cs309.travlender.ZSQ.Event;
import cs309.travlender.ZXX.EventManager;

//被唤醒时
public class Alarm extends Service {
    //RemindManager remindManager;//被观察的主题

    //List<AlarmManager> alarms;//为remindEvents设置的
    // PriorityQueue<AlarmEvent> remindEvents;//查询队列,目前只有一个元素
    AlarmEvent remindEvent;

    @Override
    public void onCreate() {
        super.onCreate();
        //remindEvents = new PriorityQueue<AlarmEvent>(10, eventComparator);
        /*updateEvent(remindEvents);//更新事件
        setAlarm(remindEvents);//设定闹钟队列
*/
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Alarm", "onStartCommand executed");
        //传递eventID过来，创建remindEvent
        remindEvent = new AlarmEvent(this, new EventManager(this).openEvent(Integer.parseInt(intent.getDataString())));
        updateOne(remindEvent);
        return START_NOT_STICKY;
    }


    //传递一个事件过来，设置闹钟
    public void updateOne(AlarmEvent a) {
        //开始时间的闹钟
        a.schedule(this, a.getStarttime());
        //有地点的行程闹钟
        if (a.isQuery == true)
            a.schedule(this, a.getDepartT());
        //用户设置的提前提醒闹钟
        if (a.getRemindEarlyT() != 0)
            a.schedule(this, a.getRemindEarlyT());

    }
}
