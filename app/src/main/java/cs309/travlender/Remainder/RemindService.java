package cs309.travlender.Remainder;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import cs309.travelender.R;
import cs309.travlender.Add_Event.ViewEventActivity;
import cs309.travlender.Remainder.AlarmEvents.AlarmEvent;
import cs309.travlender.Remainder.AlarmEvents.CommomAlarmEvent;
import cs309.travlender.Remainder.AlarmEvents.EarlyAlarmEvent;
import cs309.travlender.Remainder.AlarmEvents.TravelAlarmEvent;
import cs309.travlender.Tools.Event;
import cs309.travlender.Tools.EventManager;
import cs309.travlender.Tools.MyContext;

/**
 * Polling service
 * @Author Ryan
 * @Create 2013-7-13 上午10:18:44
 */
public class RemindService extends Service {

	public static final String ACTION = "cs309.travlender.Remainder.RemindService";
	public static final int BROADCAST = 1;
	public static final int ALARM = 2;
	public static final int INIT = 3;
	private Notification mNotification;
	private Notification.Builder nbuilder;
	private NotificationManager mManager;
	private AlarmManager alarmManager;
	private Intent alarmIntent;
	private PendingIntent alarmPendingIntent;
	private Queue<AlarmEvent> AlarmQueue;
	private Map<Integer, AlarmEvent> AlarmMap;
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
		initAlarmQueue();
	}

    @Override
	public int onStartCommand(Intent intent,int flags, int startId) {
		int type = 0;
		if(intent!=null)
			type = intent.getIntExtra("type",0);
		System.out.println("type:"+type);
		switch (type){
			case BROADCAST:
				UpdateTravelTime(intent);
				break;
			case ALARM:
				CheckingAlarm();
				break;
			case INIT:
				initAlarmQueue();
				break;
			default:
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void CheckingAlarm(){
		System.out.println("Checking:"+df.format(System.currentTimeMillis()));
		while (true){
			AlarmEvent alarmEvent = AlarmQueue.poll();
			if(alarmEvent == null)
				break;
			else if(Math.abs(alarmEvent.getAlarmtime()-System.currentTimeMillis()) < 200)
			{
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
		setAlarmManager(alarmtime,ALARM);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		initAlarmQueue();
	}

	private void UpdateTravelTime(Intent intent){
		int id = intent.getIntExtra("id",-1);
		long traveltime = intent.getLongExtra("traveltime",-1);
		if(id!=-1 && traveltime!=-1 && AlarmMap.containsKey(id)) {
			TravelAlarmEvent alarmEvent = (TravelAlarmEvent) AlarmMap.remove(id);
			alarmEvent.setTraveltime(traveltime);
			AlarmQueue.add(alarmEvent);
			System.out.println("Get broadcast");
			CheckingAlarm();
		}
	}

	private void initAlarmQueue(){
		EM = EventManager.getInstence();
		AlarmQueue = new PriorityQueue<>();
		AlarmMap = new HashMap<>();
		List<Event> EventList = EM.getEvents_aDay();
		for(Event event: EventList){
			if(event.isCommomAlarm())
				AlarmQueue.add(new CommomAlarmEvent(event));
			if(event.isEarlyAlarm())
				AlarmQueue.add(new EarlyAlarmEvent(event));
			if(event.isTravelAlarm())
			{
				new TravelThread(event);
				AlarmMap.put(event.getEventId(),new TravelAlarmEvent(event));
			}
		}
		System.out.println("Queue:"+AlarmQueue.size());
		CheckingAlarm();
	}

	private void initNotifiManager() {
		mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		int icon = R.drawable.ic_notifications_grey_600_24dp;
		nbuilder = new Notification.Builder(this);
        nbuilder.setSmallIcon(icon);
        nbuilder.setDefaults(Notification.DEFAULT_SOUND);
		nbuilder.setAutoCancel(true);
	}

	private void showNotification(int id, String title, String content) {
		nbuilder.setContentTitle(title);
		System.out.println("Show notification:"+df.format(System.currentTimeMillis()));
		nbuilder.setWhen(System.currentTimeMillis());
		//Navigator to the new activity when click the notification title
		Intent i = new Intent(this, ViewEventActivity.class);
		i.putExtra("id",id);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i,
                PendingIntent.FLAG_CANCEL_CURRENT);
        nbuilder.setContentText(content);
        nbuilder.setFullScreenIntent(pendingIntent,true);
        nbuilder.setContentIntent(pendingIntent);
        mNotification = nbuilder.build();
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		mManager.notify(0, mNotification);
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
		alarmIntent.putExtra("type", ALARM);
		alarmPendingIntent = PendingIntent.getService(this, 0,
				alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //设置唤醒时间
		alarmManager.setExact(AlarmManager.RTC_WAKEUP, waketime,
				alarmPendingIntent);
        System.out.println("唤醒时间："+df.format(waketime));
    }
	/**
	 * Polling thread
	 * @Author Ryan
	 * @Create 2013-7-13 上午10:18:34
	 */
	int count = 0;
	class TravelThread extends Thread {
		Event event;
		public TravelThread(Event event){
			this.event = event;
			start();
		}
		@Override
		public void run() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Intent intent = new Intent(MyContext.getContext(), RemindService.class);
			intent.putExtra("type",RemindService.BROADCAST);
			intent.putExtra("id",event.getEventId());
			intent.putExtra("traveltime",(long)1*60*1000);
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
