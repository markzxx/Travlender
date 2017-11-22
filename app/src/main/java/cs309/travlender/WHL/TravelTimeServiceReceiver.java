package cs309.travlender.WHL;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import cs309.travlender.WSQ.Alarm;

public class TravelTimeServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 可以在收到广播后通过intent获取查询结果，例如下面
        long seconds = Long.parseLong(intent.getStringExtra("time"));
        int id = Integer.parseInt(intent.getStringExtra("id"));
        Toast.makeText(context,"收到地图广播", Toast.LENGTH_SHORT).show();

        //启动服务，传onwayTime和eventID
        Intent updateIntent = new Intent(context, Alarm.class);
        updateIntent.setData(Uri.parse(String.valueOf(id)));
        updateIntent.putExtra("onwayTime", seconds);
        context.startService(updateIntent);


    }
}
