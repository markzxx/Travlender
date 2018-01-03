package cs309.travlender.Remainder.AlarmEvents;

import cs309.travlender.Tools.Event;

/**
 * Created by markz on 2017-12-29.
 */

public class TravelAlarmEvent extends AlarmEvent {
    private long TravelTime = -1; //初始化为-1 ， 等待更新
    private String Transport;
    private String FastTransport;
    private long FastTravelTime= -1;
    private String Weather="";
    public TravelAlarmEvent(Event father) {
        super(father);
        setAlarmtype(TransportAlarm);
        setTransport(father.getTransport());
    }

    @Override
    public String getContent() {
        String content = "";
        if(!isLate())
            content = getWeather()+getTransport()+"需要"+getformatTraveltime(getTravelTime())+", 现在要准备出发喽！";
        else if(Transport.equals(FastTransport))
            content = "来不及了！\n"+getWeather()+getTransport()+"需要"+getformatTraveltime(getTravelTime())+"，用其他交通方式也不能按时到了。";
        else if(!Transport.equals(FastTransport))
            content = "时间有点晚了！\n"+getWeather()+getTransport()+"需要"+getformatTraveltime(getTravelTime())+",如果"+getFastTransport()+"只需要"+getformatTraveltime(getFastTravelTime())+",祝你好运！";
        return content;
    }

    @Override
    public int getAlarmCode(){
        return Event.TRAVELCODE;
    }

    public String getformatTraveltime(long traveltime){
        String formattime = "";
        if(traveltime/(60*60*1000)!=0)
            formattime += traveltime/(60*60*1000)+"小时";
        if(traveltime%(60*60*1000)/(60*1000)!=0)
            formattime += traveltime%(60*60*1000)/(60*1000)+"分钟";
        return formattime;
    }

    public long getTravelTime(){
        return TravelTime;
    }  //单位毫秒

    public void setTravelTime(long transporttime){
        TravelTime = transporttime;
        setAlarmtime(getStarttime()-transporttime-1*60*1000); //提前10分钟提醒
    }
    public String getTransport() {
        return Transport;
    }

    public void setTransport(String transport) {
        Transport = transport;
    }

    public String getWeather() {
        return Weather;
    }

    public void setWeather(String weather) {
        Weather = "今天"+weather+",";
    }

    public String getFastTransport() {
        return FastTransport;
    }

    public void setFastTransport(String fastTransport) {
        FastTransport = fastTransport;
    }

    public long getFastTravelTime() {
        return FastTravelTime;
    }

    public void setFastTravelTime(long fastTravelTime) {
        FastTravelTime = fastTravelTime;
    }

}
