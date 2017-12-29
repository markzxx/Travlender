package cs309.travlender.Remainder;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.Queue;
import java.util.*;
import cs309.travelender.R;
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

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		initNotifiManager();
	}

    @Override
	public int onStartCommand(Intent intent,int flags, int startId) {
        new PollingThread().start();
	    setAlarmManager(5);
		return super.onStartCommand(intent, flags, startId);
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

	private void initAlarmQueue(){
	     AlarmQueue = new PriorityQueue<>();
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
    public void setAlarmManager(long waketime) {
        //获取AlarmManager系统服务
        AlarmManager manager = (AlarmManager) this
                .getSystemService(Context.ALARM_SERVICE);

        //包装需要执行Service的Intent
        Intent intent = new Intent(this, RemindService.class);
        intent.setAction(ACTION);
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
