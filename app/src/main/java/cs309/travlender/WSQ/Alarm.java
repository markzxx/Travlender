package cs309.travlender.WSQ;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

import cs309.travlender.ZSQ.Event;
import cs309.travlender.ZXX.EventManager;


public class Alarm extends AppCompatActivity {

    List<AlarmManager> alarms;//为remindEvents设置的
    Calendar calendar= Calendar.getInstance();
    PriorityQueue<AlarmEvent> remindEvents;//查询队列
    private AlarmManager alarm2;


    //一旦modify结束（notify：则启动这个后台活动；可以是listen）就传递event给alarms list,  refresh queryQueue

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_alarm);//在后台不需要界面
        remindEvents = new PriorityQueue<>(10, eventComparator);

        updateEvent(remindEvents);//更新事件

        setAlarm(remindEvents);//设定闹钟队列


    }

    //软件打开时添加所有事件进来
    public PriorityQueue<AlarmEvent> updateEvent(PriorityQueue<AlarmEvent> remindEvents){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String currentTime = df.format(new Date());// new Date()为获取当前系统时间
        Date d = new Date();
        d.setMonth(d.getMonth()+1);
        String endTime = df.format(d);
        EventManager em = new EventManager(this);
        remindEvents = sortEvent(em.getEvents(currentTime, endTime), remindEvents);


        return remindEvents;
    }
    //将事件列表变成一个优先队列
    public PriorityQueue<AlarmEvent> sortEvent(List<Event> Events, PriorityQueue<AlarmEvent> remindEvents){

        for(Event e: Events){
            remindEvents.add(new AlarmEvent(this, e));
        }

        return remindEvents;
    }
    //比较器
    public static Comparator<AlarmEvent> eventComparator = new Comparator<AlarmEvent>(){

        @Override
        public int compare(AlarmEvent a1, AlarmEvent a2) {//从小到大排序
            return (int) (Math.min(a1.getDepartT(),a1.getRemindEarlyT()) - Math.min(a2.getDepartT(),a2.getRemindEarlyT()));
        }
    };
    //传递一个事件过来，设置闹钟，加上提醒内容，计算提醒时间（调用地图）
    public void updateOne(Event e){

        Intent intent =new Intent(this, AlarmReceiver.class);
        PendingIntent sender= PendingIntent.getBroadcast(this, 0, intent, 0);
        calendar.setTimeInMillis(e.getStarttime());
        AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);//设定闹钟
        alarms.add(alarm);
    }
    //查询：渐进地update（一小时，半小时，五分钟前）

    //set all alarm
    public void setAlarm(PriorityQueue<AlarmEvent> remindEvents){
        for(int i = 0; i < remindEvents.size(); i++){
            Intent intent =new Intent(this, AlarmReceiver.class);
            PendingIntent sender= PendingIntent.getBroadcast(this, 0, intent, 0);
            //设定在开始时间提醒的闹钟
            calendar.setTimeInMillis(remindEvents.peek().getStarttime());//将时间设定为最优先的
            AlarmManager alarm1=(AlarmManager)getSystemService(ALARM_SERVICE);
            alarm1.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);//设定闹钟
            alarms.add(alarm1);

            //设定在用户选择提前提醒的闹钟
            if(remindEvents.peek().fatherE.getRemindtime() == 0){
                calendar.setTimeInMillis(remindEvents.peek().getRemindEarlyT());//将时间设定为最优先的
                AlarmManager alarm2= (AlarmManager)getSystemService(ALARM_SERVICE);
                alarm2.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);//设定闹钟
                alarms.add(alarm2);
            }

            //设定需要路程的出发提醒闹钟
            if(remindEvents.peek().getOnWayTime() == 0){
                calendar.setTimeInMillis(remindEvents.peek().getOnWayTime());//将时间设定为最优先的
                AlarmManager alarm2= (AlarmManager)getSystemService(ALARM_SERVICE);
                alarm2.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);//设定闹钟
                alarms.add(alarm2);
            }

        }
    }

    //get time
    public String transferLongToDate(String dateFormat, Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = new Date(millSec);
        return sdf.format(date);
    }



}
