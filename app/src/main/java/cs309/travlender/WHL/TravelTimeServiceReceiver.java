package cs309.travlender.WHL;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import cs309.travlender.Remainder.RemindService;

public class TravelTimeServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 可以在收到广播后通过intent获取查询结果，例如下面
        long traveltime = Long.parseLong(intent.getStringExtra("time"))*1000;
        int id = Integer.parseInt(intent.getStringExtra("id"));
        Toast.makeText(context,intent.getStringExtra("time")+"收到地图广播", Toast.LENGTH_SHORT).show();

        //启动服务，传onwayTime和eventID
        Intent updateIntent = new Intent(context, RemindService.class);
        updateIntent.putExtra("type", RemindService.BROADCAST);
        updateIntent.putExtra("id", id);
        updateIntent.putExtra("traveltime", traveltime);
        context.startService(updateIntent);
        TravelTimeService.querySet.add(id);
    }
}
