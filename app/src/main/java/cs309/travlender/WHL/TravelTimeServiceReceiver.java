package cs309.travlender.WHL;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import cs309.travlender.Remainder.RemindService;

public class TravelTimeServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 可以在收到广播后通过intent获取查询结果，例如下面
        Bundle bundle = intent.getExtras();
        int fastest_transport_travel_time = bundle.getInt("fastest_transport_travel_time");
        String fastest_transport = bundle.getString("fastest_transport");
        int required_transport_travel_time = bundle.getInt("required_transport_travel_time");
        String required_transport = bundle.getString("required_transport");
        int id = bundle.getInt("id");

        //启动服务，传onwayTime和eventID
        Intent updateIntent = new Intent(context, RemindService.class);
        updateIntent.putExtra(RemindService.TRAVELTIME, required_transport_travel_time);
        updateIntent.putExtra(RemindService.FASTTRANSPORT, fastest_transport);
        updateIntent.putExtra(RemindService.FASTTRAVELTIME, (long) fastest_transport_travel_time);
        updateIntent.putExtra(RemindService.ID, id);
        context.startService(updateIntent);
        TravelTimeService.querySet.add(id);
    }
}
