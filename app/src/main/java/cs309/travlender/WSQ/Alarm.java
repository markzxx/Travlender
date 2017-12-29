package cs309.travlender.WSQ;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import cs309.travlender.Remainder.AlarmEvent;
import cs309.travlender.Tools.Event;
import cs309.travlender.Tools.EventManager;

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
        Event event = EventManager.getInstence().getEvent(Integer.parseInt(intent.getDataString()));
        //传递eventID过来，创建remindEvent
//        remindEvent = new AlarmEvent(event, intent.getLongExtra("onwayTime",0));
        updateOne(remindEvent);
        return START_NOT_STICKY;
    }

//type: StartTime; DepartTime; SetRemindTime
    //传递一个事件过来，设置闹钟
    public void updateOne(AlarmEvent a) {
        //开始时间的闹钟
        a.schedule(this, a.getStarttime(), "StartTime");
        //有地点的行程闹钟
//        if (a.isQuery == true)
//            a.schedule(this, a.getDepartT(), "DepartTime");
        //用户设置的提前提醒闹钟
        if (a.getRemindEarlyTime() != 0)
            a.schedule(this, a.getRemindEarlyTime(), "SetRemindTime");

    }
}
