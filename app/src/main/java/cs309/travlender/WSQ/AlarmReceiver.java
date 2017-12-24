package cs309.travlender.WSQ;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.WindowManager;

import java.io.IOException;

/**
 * Created by alicewu on 11/19/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private Vibrator vibrator;
    private String remindType;//type: StartTime; DepartTime; SetRemindTime
    private MediaPlayer mediaPlayer;


    @Override
    public void onReceive(Context context, Intent intent) {

        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(context)) {
                Intent intent1 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                context.startActivity(intent1);
            }
        }

        String location = intent.getStringExtra("location");
        long onwayTime = intent.getLongExtra("onwayTime", 0);
        String bestTransport = intent.getStringExtra("bestTransport");
        long remindtime = intent.getLongExtra("remindtime", 0);
        String title = intent.getStringExtra("title");
        remindType = intent.getDataString();
        showAlarmDialog(context, location, onwayTime, bestTransport, remindtime, title);

    }


    //UI接口
    private void showAlarmDialog(Context context, String location, long onwayTime, String bestTransport, long remindtime, String title) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        playMusicAndVibrate(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        switch (remindType) {
            case "StartTime":
                setDialog(builder, title, "时间到了");
                break;
            case "DepartTime":
                setDialog(builder, title, String.format("现在就要出发去%s，%s要花%d分钟",
                        location, bestTransport, onwayTime));
                break;
            case "SetRemindTime":
                setDialog(builder, title, String.format("%d分钟后开始",
                        remindtime));
                break;
        }

        AlertDialog dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    private void setDialog(AlertDialog.Builder builder, String title, String message) {
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            mediaPlayer = null;


                        }
                    }
                });

    }


    /**
     * 播放音乐
     *
     * @param context
     */
    private void playMusicAndVibrate(Context context) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.setVolume(100f, 100f);
            mediaPlayer.setDataSource(context, RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
            vibrator.vibrate(new long[]{1000, 50, 1000, 50}, 0);//加了震动权限


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
