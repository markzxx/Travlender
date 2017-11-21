package cs309.travlender.WSQ;

import android.app.AlarmManager;
import android.content.ContentValues;

import java.util.Date;

import cs309.travlender.ZSQ.Event;

/**
 * Created by alicewu on 11/20/17.
 */

public class AlarmEvent extends Event {
    AlarmManager alarm;//对应闹钟的指针
    Event fatherE;//指向原始闹钟的指针

    boolean isQuery = true;//if location is null
    long onwayTime = 0;//on way time, if location is not null
    long departTime;//if location is not null
    long remindEarly;//if set by user
    long startTime;
    //查询队列的优先级是min（deparTime,remindBefore）,由小到大
    //when happen，it must remind.
    String bestTransport;

    public AlarmEvent(Alarm value, Event father){
        //super((ContentValues)value); 在
        fatherE = father;
        startTime = fatherE.getStarttime();
        if(super.getLocation() == null){//无地点
            onwayTime = 0;//路程时间为零
            isQuery = false;//不需要查询
        }
        else{
            updateBestTransport(new Date());
            isQuery = true;
        }
        departTime = fatherE.getStarttime() - onwayTime;
        remindEarly = fatherE.getStarttime() - fatherE.getRemindtime();
    }



    //每次查询都要调用
    public void updateBestTransport(Date current){
        onwayTime =map.getOnWayTime(fatherE.getLocation(), fatherE.getStarttime());
        bestTransport = map.getBestTrans(fatherE.getTransport(),fatherE.getLocation(), fatherE.getStarttime());//优化交通方式, 交通方式的比较

    }


    public long getDepartT(){
        return departTime;
    }
    public long getRemindEarlyT(){
        return remindEarly;
    }
    public long getOnWayTime(){
        return onwayTime;
    }
}
