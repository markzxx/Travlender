package cs309.travlender.Remainder;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
/**
 * Polling Tools
 * @Author Ryan
 * @Create 2013-7-13 上午10:14:43
 */
public class PollingUtils {

	/**
	 * @param context
	 * @param seconds
	 * @param cls
	 * @param action
	 */ 
	public static void startPollingService(Context context, int seconds, Class<?> cls,String action) {
        //获取AlarmManager系统服务
		AlarmManager manager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

        //包装需要执行Service的Intent
		Intent intent = new Intent(context, cls);
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //触发服务的起始时间
		long triggerAtTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        //使用AlarmManger的setRepeating方法设置定期执行的时间间隔（seconds秒）和需要执行的Service
		manager.setWindow(AlarmManager.RTC_WAKEUP, triggerAtTime+5000,
				100, pendingIntent);
        System.out.println(triggerAtTime);
	}

	/**
	 * 
	 * @param context
	 * @param cls
	 * @param action
	 */
	public static void stopPollingService(Context context, Class<?> cls,String action) {
		AlarmManager manager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, cls);
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		manager.cancel(pendingIntent);
	}
}
