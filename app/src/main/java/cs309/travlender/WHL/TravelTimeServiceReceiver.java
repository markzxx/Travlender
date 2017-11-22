package cs309.travlender.WHL;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class TravelTimeServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 可以在收到广播后通过intent获取查询结果，例如下面  
//        String time = intent.getStringExtra("time");
//        Toast.makeText(context,"收到广播 "+time, Toast.LENGTH_SHORT).show();

    }
}
