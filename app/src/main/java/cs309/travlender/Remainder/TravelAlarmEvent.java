package cs309.travlender.Remainder;

import cs309.travlender.Tools.Event;

/**
 * Created by markz on 2017-12-29.
 */

public class TravelAlarmEvent extends AlarmEvent {
    private long Traveltime = -1; //初始化为-1 ， 等待更新
    private String Transport;
    public TravelAlarmEvent(Event father) {
        super(father);
        setAlarmtype(TransportAlarm);
        setTransport(father.getTransport());
    }
    public long getTraveltime(){
        return Traveltime;
    }
    public void setTraveltime(long transporttime){
        Traveltime = transporttime;
        setAlarmtime(getStarttime()-transporttime);
    }
    public String getTransport() {
        return Transport;
    }

    public void setTransport(String transport) {
        Transport = transport;
    }

}
