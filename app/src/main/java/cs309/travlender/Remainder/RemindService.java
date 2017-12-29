package cs309.travlender.Remainder;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.List;

import cs309.travelender.R;
import cs309.travlender.Tools.Event;
import cs309.travlender.Tools.EventManager;

/**
 * Polling service
 * @Author Ryan
 * @Create 2013-7-13 上午10:18:44
 */
public class RemindService extends Service {

	public static final String ACTION = "cs309.travlender.Remainder.RemindService";
	
	private Notification mNotification;
	private Notification.Builder nbuilder;
	private NotificationManager mManager;
	private Queue<AlarmEvent> AlarmQueue;
	private Map<Integer, AlarmEvent> AlarmMap;
	private EventManager EM;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		initNotifiManager();
		initAlarmQueue();
	}

    @Override
	public int onStartCommand(Intent intent,int flags, int startId) {
		String type = intent.getStringExtra("type");
		switch (type){
			case "broadcast":
				int id = intent.getIntExtra("id",-1);
				long traveltime = intent.getLongExtra("traveltime",-1);
				UpdateTravelTime(id, traveltime);
				break;
			case "alarm":
				break;

		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void CheckingAlarm(){
		AlarmEvent alarmEvent = AlarmQueue.poll();
	}

	private void SetingAlarm(){

	}

	private void UpdateTravelTime(int id, long traveltime){
		if(id!=-1 && traveltime!=-1) {
			TravelAlarmEvent alarmEvent = (TravelAlarmEvent) AlarmMap.remove(id);
			alarmEvent.setTraveltime(traveltime);
			AlarmQueue.add(alarmEvent);
		}
	}

	private void initAlarmQueue(){
		EM = EventManager.getInstence();
		AlarmQueue = new PriorityQueue<>();
		AlarmMap = new HashMap<>();
		List<Event> EventList = EM.getEvents_aDay();
		for(Event event: EventList){
			AlarmQueue.add(new CommomAlarmEvent(event));
			if(event.getSmartRemind()==1)
				AlarmQueue.add(new EarlyAlarmEvent(event));
			if(event.getSmartRemind()==1)
			{
//				TransportTime(event);
				AlarmMap.put(event.getEventId(),new TravelAlarmEvent(event));
			}
		}
	}

	private void initNotifiManager() {
		mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		int icon = R.drawable.ic_notifications_grey_600_24dp;
		nbuilder = new Notification.Builder(this);
        nbuilder.setContentTitle("悬挂式通知");
        nbuilder.setSmallIcon(icon);
        nbuilder.setDefaults(Notification.DEFAULT_SOUND);
		nbuilder.setAutoCancel(true);
	}

	private void showNotification(String content) {
		nbuilder.setWhen(System.currentTimeMillis());
		//Navigator to the new activity when click the notification title
		Intent i = new Intent(this, MessageActivity.class);
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
    public void setAlarmManager(long waketime, String type) {
        //获取AlarmManager系统服务
        AlarmManager manager = (AlarmManager) this
                .getSystemService(Context.ALARM_SERVICE);

        //包装需要执行Service的Intent
        Intent intent = new Intent(this, RemindService.class);
        intent.setAction(ACTION);
        intent.putExtra("type",type);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //使用AlarmManger的setRepeating方法设置定期执行的时间间隔（seconds秒）和需要执行的Service
        manager.setWindow(AlarmManager.RTC_WAKEUP, waketime,
                100, pendingIntent);
        System.out.println(System.currentTimeMillis());
    }
	/**
	 * Polling thread
	 * @Author Ryan
	 * @Create 2013-7-13 上午10:18:34
	 */
	int count = 0;
	class PollingThread extends Thread {
		@Override
		public void run() {
		    count++;
			System.out.println("Polling..."+count+".."+ System.currentTimeMillis());
            showNotification("Message "+count);
            System.out.println("New message!");
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("Service:onDestroy");
	}

}
