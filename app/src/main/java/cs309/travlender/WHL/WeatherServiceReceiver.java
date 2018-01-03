package cs309.travlender.WHL;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WeatherServiceReceiver extends BroadcastReceiver {

    public static String BROADCAST_SIGNAL = "com.example.dell.map.WeatherServiceReceiver";

    public static String getReportTime = "com.example.dell.map.WeatherServiceReceiver.getReportTime";
    public static String getCity = "com.example.dell.map.WeatherServiceReceiver.getCity";
    public static String getWeather = "com.example.dell.map.WeatherServiceReceiver.getWeather";
    public static String getTemperature = "com.example.dell.map.WeatherServiceReceiver.getTemperature";
    public static String getHumidity = "com.example.dell.map.WeatherServiceReceiver.getHumidity";
    public static String getWindPower = "com.example.dell.map.WeatherServiceReceiver.getWindPower";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("WeatherServiceReceiver", "收到天气广播");
        String ReportTime = intent.getStringExtra(getReportTime);
        String City = intent.getStringExtra(getCity);
        String Weather = intent.getStringExtra(getWeather);
        String Temperature = intent.getStringExtra(getTemperature);
        String Humidity = intent.getStringExtra(getHumidity);
        String WindPower = intent.getStringExtra(getWindPower);
        int id = Integer.parseInt(intent.getStringExtra("id"));
        WeatherService.querySet.add(id);
        // 与提醒功能相关的代码

    }
}