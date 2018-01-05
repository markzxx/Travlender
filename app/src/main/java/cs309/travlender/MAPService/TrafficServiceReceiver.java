package cs309.travlender.MAPService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cs309.travlender.Remainder.RemindService;

public class TrafficServiceReceiver extends BroadcastReceiver {

    public static String BROADCAST_SIGNAL = "com.example.dell.map.TrafficServiceReceiver";

    public static String GET_TRAFFIC_SITUATION = "com.example.dell.map.TrafficServiceReceiver.getTRAFFIC_SITUATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        int id = Integer.parseInt(intent.getStringExtra("id"));
        TrafficService.querySet.add(id);
        String traffic_situation = intent.getStringExtra(GET_TRAFFIC_SITUATION);
        Log.d("TrafficServiceReceiver", "收到交通广播 "+id);
        // 与提醒功能相关的代码
        Intent updateIntent = new Intent(context, RemindService.class);
        updateIntent.putExtra(RemindService.TYPE, RemindService.TYPE_TRAFFIC);
        updateIntent.putExtra(RemindService.TRAFFIC, traffic_situation);
        updateIntent.putExtra(RemindService.ID, id);
        context.startService(updateIntent);
    }
}
