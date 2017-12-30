//package cs309.travlender.WSQ;
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//
//import java.io.Serializable;
//import java.util.Calendar;
//import java.util.Date;
//
//import cs309.travlender.Tools.Event;
//
///**
// * Created by alicewu on 11/20/17.
// */
//
//public class AlarmEvent implements Serializable{
//    private AlarmManager alarm;//对应闹钟的指针
//    private Event fatherE;//指向原始闹钟的指针
//
//    boolean isSmart = true;//是否需要交通规划
//    boolean isEarly = true;//是否需要提前提醒
//    private long onwayTime = -1;//on way time, if location is not null(s)
//    private long departTime = -1;//if location is not null(ms) 行程开始的出发时间，算上路程，格林尼治时间
//    private long remindEarly = -1;//if set by user(1 minutes = 60s) 提前多少minute提醒，由用户设置
//    private long remindEarlyTime = -1; //用于提前提醒的格林尼治时间
//    private long startTime = -1;//(1000ms = 1s) 必须提醒的开始时间，格林尼治时间
//    //查询队列的优先级是min（deparTime,remindBefore）,由小到大
//    //when happen，it must remind.
//    private String bestTransport = "none";
//
//    public AlarmEvent(Event father) {
//        fatherE = father;
//
//        startTime = fatherE.getStarttime();
//        if (fatherE.getLocation().equals("无") || fatherE.getSmartRemind() == 0) {//无地点或不需要提醒
//            isSmart = false;
//        }else {
//            bestTransport = father.getTransport();
//        }
//        if (fatherE.getRemindtime() == 0) {//不需要提前提醒
//            isEarly = false;
//        }else {
//            remindEarly = fatherE.getRemindtime();
//            remindEarlyTime = fatherE.getStarttime() - remindEarly*60000;//in ms(格林尼治时间）
//        }
//    }
//
//    //设置闹钟,给定时间设置闹钟, type: StartTime; DepartTime; SetRemindTime
//    public void schedule(Context context, long remindTime, String type) {
//        Intent myIntent = new Intent(context, AlarmReceiver.class);//在闹钟设定时刻通知alarmAlert
//
//        myIntent.putExtra("location", fatherE.getLocation());
//        myIntent.putExtra("onwayTime", onwayTime);
//        myIntent.putExtra("bestTransport", bestTransport);
//        myIntent.putExtra("remindtime", fatherE.getRemindtime());
//        myIntent.putExtra("title", fatherE.getTitle());
//
//        myIntent.setData(Uri.parse(type));
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, getRealAlarmTime(remindTime).getTimeInMillis(), pendingIntent);
//    }
//
//    public Calendar getRealAlarmTime(long remindTime) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(remindTime);
//        return calendar;
//    }
//
//    //每次查询都要调用地图
//    public void updateBestTransport(Date current) {
////        onwayTime = map.getOnWayTime(fatherE.getLocation(), fatherE.getStarttime());
////        bestTransport = map.getBestTrans(fatherE.getTransport(), fatherE.getLocation(), fatherE.getStarttime());//优化交通方式, 交通方式的比较
//
//    }
//
//    public Event getFatherE() {
//        return fatherE;
//    }
//
//    public long getDepartT() {
//        return departTime;
//    }
//
//    public long getRemindEarly() {
//        return remindEarly;
//    }
//
//    public long getOnWayTime() {
//        return onwayTime;
//    }
//
//    public long getStarttime() {
//        return startTime;
//    }
//
//    public String getBestTransport(){return bestTransport; }
//
//    public long getRemindEarlyTime(){ return remindEarlyTime; }
//
//}
