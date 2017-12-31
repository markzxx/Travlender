package cs309.travlender.Remainder.AlarmEvents;

import cs309.travlender.Tools.Event;

/**
 * Created by alicewu on 11/20/17.
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
