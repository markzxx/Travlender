package cs309.travlender.Remainder;

import cs309.travlender.Tools.Event;

/**
 * Created by markz on 2017-12-29.
 */

public class EarlyAlarmEvent extends AlarmEvent {
    private int Earlytime;
    public EarlyAlarmEvent(Event father) {
        super(father);
        setAlarmtype(EarlyAlarm);
        Earlytime = father.getEarlytime(); //Starttime单位是毫秒，Remindtime是分钟
        setAlarmtime(father.getStarttime()- Earlytime*60000);
    }
    public int getEarlytime(){
        return Earlytime;
    }
}
