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
        long traveltime = intent.getLongExtra("time",0);
        int id = intent.getIntExtra("id",0);
        String transport = intent.getStringExtra("transport");
        Toast.makeText(context,traveltime+"收到地图广播", Toast.LENGTH_SHORT).show();

        //启动服务，传onwayTime和eventID
        Intent updateIntent = new Intent(context, RemindService.class);
        updateIntent.putExtra(RemindService.TYPE, RemindService.TYPE_TRAVELTIME);
        updateIntent.putExtra(RemindService.ID, id);
        updateIntent.putExtra(RemindService.TRAVELTIME, traveltime*1000); //从秒转换成毫秒
        context.startService(updateIntent);
        TravelTimeService.querySet.add(id);
    }
}
