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

    @Override
    public String getContent() {
        return getTransport()+"需要"+getformatTraveltime()+", 现在要准备出发喽！";
    }

    @Override
    public int getAlarmCode(){
        return Event.TRAVELCODE;
    }

    public String getformatTraveltime(){
        String formattime = "";
        long traveltime = getTraveltime();
        if(traveltime/(60*60*1000)!=0)
            formattime += traveltime/(60*60*1000)+"小时";
        if(traveltime%(60*60*1000)/(60*1000)!=0)
            formattime += traveltime%(60*60*1000)/(60*1000)+"分钟";
        return formattime;
    }

    public long getTraveltime(){
        return Traveltime;
    }  //单位毫秒

    public void setTraveltime(long transporttime){
        Traveltime = transporttime;
        setAlarmtime(getStarttime()-transporttime-1*60*1000); //提前10分钟提醒
    }
    public String getTransport() {
        return Transport;
    }

    public void setTransport(String transport) {
        Transport = transport;
    }

}
