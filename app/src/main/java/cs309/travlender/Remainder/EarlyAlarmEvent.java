package cs309.travlender.Remainder;

import cs309.travlender.Tools.Event;

/**
 * Created by markz on 2017-12-29.
 */

public class EarlyAlarmEvent extends AlarmEvent {

    public EarlyAlarmEvent(Event father) {
        super(father);
        setAlarmtype(EarlyAlarm);
        setAlarmtime(father.getStarttime()- getEarlytime()*60000);  //Starttime单位是毫秒，Remindtime是分钟
    }

    @Override
    public String getContent() {
        return getformatEarlytime();
    }

    @Override
    public int getAlarmCode(){
        return Event.EARLYCODE;
    }

    public String getformatEarlytime(){
        String formattime = "提前";
        int earlytime = getEarlytime();
        if(earlytime/60!=0)
            formattime += earlytime/60+"小时";
        if(earlytime%60!=0)
            formattime += earlytime%60+"分钟";
        return formattime+"提醒";
    }

    public int getEarlytime(){
        return getFatherEvent().getEarlytime();
    }

}
