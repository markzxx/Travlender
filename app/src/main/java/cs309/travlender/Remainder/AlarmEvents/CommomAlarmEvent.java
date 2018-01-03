package cs309.travlender.Remainder.AlarmEvents;

import cs309.travlender.Tools.Event;

/**
 * Created by markz on 2017-12-29.
 */

public class CommomAlarmEvent extends AlarmEvent{

    public CommomAlarmEvent(Event father) {
        super(father);
        setAlarmtype(CommomAlarm);
        setAlarmtime(father.getStarttime());
    }

    public String getContent(){
        return df.format(getAlarmtime())+" !";
    }

    @Override
    public int getAlarmCode(){
        return Event.COMMOMCODE;
    }
}
