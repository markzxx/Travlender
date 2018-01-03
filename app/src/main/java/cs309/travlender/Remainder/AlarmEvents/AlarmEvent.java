package cs309.travlender.Remainder.AlarmEvents;

import java.text.SimpleDateFormat;

import cs309.travlender.Tools.Event;

/**
 * Created by markz on 2017-12-29.
 */

public abstract class AlarmEvent implements Comparable{
    final int TransportAlarm = 1;
    final int EarlyAlarm = 2;
    final int CommomAlarm = 3;
    protected boolean late = false;
    private Event fatherEvent;
    private int Alarmtype;//闹铃类型
    private long Starttime;
    private long Alarmtime = -1; //初始化为-1 ， 等待更新
    SimpleDateFormat df = new SimpleDateFormat("HH:mm");


    public AlarmEvent(Event father) {
        fatherEvent = father;
        Starttime = father.getStarttime();
    }

    abstract public String getContent();

    public String getTitle(){ return fatherEvent.getTitle(); }

    public int getID(){ return fatherEvent.getEventId(); }

    public Event getFatherEvent() {
        return fatherEvent;
    }

    public long getAlarmtime() {
        return Alarmtime;
    }

    public int getAlarmtype() {
        return Alarmtype;
    }

    public void setAlarmtime(long Alarmtime) {
        this.Alarmtime = Alarmtime;
    }

    public void setAlarmtype(int Alarmtype) {
        this.Alarmtype = Alarmtype;
    }

    public long getStarttime() {
        return Starttime;
    }

    public void setStarttime(long starttime) {
        Starttime = starttime;
    }

    abstract public int getAlarmCode();

    public int compareTo(Object o){
        if (Alarmtime > ((AlarmEvent)o).getAlarmtime())
            return 1;
        else
            return -1;
    }

    public void setLate(boolean late) {
        this.late = late;
    }

    public boolean isLate() {
        return late;
    }
}
