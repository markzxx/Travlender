package cs309.travlender.Remainder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import cs309.travelender.R;
import cs309.travlender.EventActivity.ViewEventActivity;
import cs309.travlender.Remainder.AlarmEvents.AlarmEvent;
import cs309.travlender.Remainder.AlarmEvents.CommomAlarmEvent;
import cs309.travlender.Remainder.AlarmEvents.EarlyAlarmEvent;
import cs309.travlender.Remainder.AlarmEvents.TravelAlarmEvent;
import cs309.travlender.Tools.Event;
import cs309.travlender.Tools.EventManager;
import cs309.travlender.Tools.MyContext;
import cs309.travlender.WHL.WeatherService;

import static android.support.v4.app.NotificationCompat.CATEGORY_REMINDER;

public class RemindService extends Service {

	public static final String ACTION = "cs309.travlender.Remainder.RemindService";
	public static final int TYPE_ALARM = 1;
	public static final int TYPE_INIT = 2;
	public static final int TYPE_TRAVELTIME = 3;
	public static final int TYPE_WEATHER = 4;
	public static final int TYPE_TRAFFIC = 5;
	public static final int TYPE_FASTTRANSPORT = 6;
	public static final String ID = "ID";
	public static final String TYPE = "TYPE";
	public static final String TRAVELTIME = "TYPE_TRAVELTIME";
	public static final String FASTTRANSPORT = "TYPE_FASTTRANSPORT";
	public static final String FASTTRAVELTIME = "FASTTRAVELTIME";
	public static final String WEATHER = "TYPE_WEATHER";

	private Notification mNotification;
	private Notification.Builder nbuilder;
	private NotificationManager mManager;
	private AlarmManager alarmManager;
	private Intent alarmIntent;
	private PendingIntent alarmPendingIntent;
	private Queue<AlarmEvent> AlarmQueue;
	private Map<Integer, AlarmEvent> TravlTimeMap;
	private Map<Integer, AlarmEvent> WeatherMap;
	private EventManager EM;
	private long NextAlarmTime;
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		NextAlarmTime = Long.MAX_VALUE;
		initNotifiManager();
		initAlarmManager();
	}

    @Override
	public int onStartCommand(Intent intent,int flags, int startId) {
		int type = 0;
		if(intent!=null)
			type = intent.getIntExtra(TYPE,0);
		System.out.println("type:"+type);
		switch (type){
			case TYPE_TRAVELTIME:
				UpdateTravelTime(intent);
				break;
			case TYPE_WEATHER:
				UpdateWeather(intent);
				break;
			case TYPE_ALARM:
				CheckingAlarm();
				break;
			default:
				initAlarmQueue();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void CheckingAlarm(){
		System.out.println("Checking:"+df.format(System.currentTimeMillis()));
		while (true){
			AlarmEvent alarmEvent = AlarmQueue.poll();
			if(alarmEvent == null)
				break;
			else if(alarmEvent.getAlarmtime() < System.currentTimeMillis()-60000) //超时一分钟以上，调用超时提醒方式
			{
				alarmEvent.setLate(true);
				showNotification(alarmEvent.getID(), alarmEvent.getTitle(), alarmEvent.getContent());
				alarmEvent.getFatherEvent().setAlarmStatus(alarmEvent.getAlarmCode());
			}
			else if(Math.abs(alarmEvent.getAlarmtime() - System.currentTimeMillis()) < 60000){ //时间在一分钟内，直接提醒
				showNotification(alarmEvent.getID(), alarmEvent.getTitle(), alarmEvent.getContent());
				alarmEvent.getFatherEvent().setAlarmStatus(alarmEvent.getAlarmCode());
			}
			else if(alarmEvent.getAlarmtime() > System.currentTimeMillis()) {
				AlarmQueue.add(alarmEvent);
				if (NextAlarmTime < System.currentTimeMillis() || alarmEvent.getAlarmtime() < NextAlarmTime)
					SetNextAlarm(alarmEvent.getAlarmtime());
				break;
			}
		}

		System.out.println("Next Alarm time:"+df.format(NextAlarmTime));
	}

	private void SetNextAlarm(long alarmtime){
		NextAlarmTime = alarmtime;
		setAlarmManager(alarmtime, TYPE_ALARM);
		Toast.makeText(this,"下一次提醒时间"+df.format(NextAlarmTime), Toast.LENGTH_SHORT).show();
		initAlarmQueue();
	}

	private void UpdateTravelTime(Intent intent){
		int id = intent.getIntExtra(ID,-1);
		long traveltime = intent.getLongExtra(TRAVELTIME,-1);
		String fastTransport = intent.getStringExtra(FASTTRANSPORT);
		long fastTraveltime = intent.getLongExtra(FASTTRAVELTIME,-1);
		if(id!=-1 && traveltime!=-1 && fastTraveltime!=-1 && TravlTimeMap.containsKey(id)) {
			TravelAlarmEvent alarmEvent = (TravelAlarmEvent) TravlTimeMap.remove(id);
			alarmEvent.setTravelTime(traveltime);
			alarmEvent.setFastTransport(fastTransport);
			alarmEvent.setFastTravelTime(fastTraveltime);
			AlarmQueue.add(alarmEvent);
			System.out.println("Get broadcast: "+alarmEvent.getTitle()+traveltime/1000+"s");
			CheckingAlarm();
		}
	}

	private void UpdateWeather(Intent intent){
		int id = intent.getIntExtra(ID,-1);
		String weather = intent.getStringExtra(WEATHER);
		Log.d("RemindService","88++88 是否包含key：" + WeatherMap.containsKey(id));
		if (WeatherMap.get(id)==null){
			return;
		}
		((TravelAlarmEvent) (WeatherMap.get(id))).setWeather(weather);
		System.out.println("Get weather: "+id);
	}

	private void initAlarmQueue(){
		EM = EventManager.getInstence();
		AlarmQueue = new PriorityQueue<>();
		TravlTimeMap = new HashMap<>();
		WeatherMap = new HashMap<>();
		List<Event> EventList = EM.getEvents_aDay();
		for(Event event: EventList){
			if(event.isCommomAlarm())
				AlarmQueue.add(new CommomAlarmEvent(event));
			if(event.isEarlyAlarm())
				AlarmQueue.add(new EarlyAlarmEvent(event));
			if(event.isTravelAlarm())
			{
				new TravelThread(event, this);
				TravelAlarmEvent travelAlarmEvent = new TravelAlarmEvent(event);
				TravlTimeMap.put(event.getEventId(), travelAlarmEvent);
				WeatherMap.put(event.getEventId(), travelAlarmEvent);
			}

		}
		System.out.println("Queue:"+AlarmQueue.size());
		CheckingAlarm();
	}

	private void initNotifiManager() {
		mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		int icon = R.drawable.ic_notifications_grey600_48px;
		nbuilder = new Notification.Builder(this);
        nbuilder.setSmallIcon(icon);
        nbuilder.setDefaults(Notification.DEFAULT_SOUND);
		nbuilder.setAutoCancel(true);
		nbuilder.setPriority(Notification.PRIORITY_HIGH);
		nbuilder.setCategory(CATEGORY_REMINDER);
	}

	private void showNotification(int id, String title, String content) {
		nbuilder.setContentTitle(title);
		System.out.println("Show notification:"+df.format(System.currentTimeMillis()));
		nbuilder.setWhen(System.currentTimeMillis());
		//Navigator to the new activity when click the notification title
		Intent i = new Intent(this, ViewEventActivity.class);
		i.putExtra(ID,id);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i,
                PendingIntent.FLAG_CANCEL_CURRENT);
        nbuilder.setContentText(content);
        nbuilder.setFullScreenIntent(pendingIntent,true);
        nbuilder.setContentIntent(pendingIntent);
        mNotification = nbuilder.build();
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		mManager.notify(0, mNotification);
		Intent show = new Intent(this, MessageActivity.class);
		show.putExtra("title", title);
		show.putExtra("content",content);
		startActivity(show);
	}

	//设置AlarmManager
	public void initAlarmManager(){

	}
    public void setAlarmManager(long waketime, int type) {
		//获取AlarmManager系统服务
		alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

		//包装需要执行Service的Intent
		alarmIntent = new Intent(this, RemindService.class);
		alarmIntent.setAction(ACTION);
		alarmIntent.putExtra(TYPE, TYPE_ALARM);
		alarmPendingIntent = PendingIntent.getService(this, 0,
				alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //设置唤醒时间
		alarmManager.setExact(AlarmManager.RTC_WAKEUP, waketime,
				alarmPendingIntent);
        System.out.println("唤醒时间："+df.format(waketime));
    }

	class TravelThread extends Thread {
		Event event;
		Context context;
		public TravelThread(Event event, Context context){
			this.event = event;
			this.context = context;
			start();
		}
		@Override
		public void run() {
//			TravelTimeService.startServiceTravelTime(context, event.getLatitude(), event.getLongitude(), event.getTransport(), event.getEventId());
			WeatherService.startServiceWeatherWithDestination(context, event.getLatitude(), event.getLongitude(), event.getEventId());
			Intent intent = new Intent(MyContext.getContext(), RemindService.class);
			intent.putExtra(TYPE,RemindService.TYPE_TRAVELTIME);
			intent.putExtra(ID,event.getEventId());
			intent.putExtra(TRAVELTIME,(long)1*60*10000);
			intent.putExtra(FASTTRANSPORT,"自驾");
			intent.putExtra(FASTTRAVELTIME,(long)1*60*1000);
			MyContext.getContext().startService(intent);
			System.out.println("Traveltime sent "+event.getTitle());
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("Service:onDestroy");
	}

}
