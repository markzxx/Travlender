package cs309.travlender.WSQ;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.view.WindowManager;

import java.io.IOException;

/**
 * Created by alicewu on 11/19/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private Vibrator vibrator;

    private MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        AlarmEvent alarm = (AlarmEvent) bundle.getSerializable("alarm");

        showAlarmDialog(context, alarm);

    }

    //UI接口
    private void showAlarmDialog(Context context, AlarmEvent alarmEvent) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        playMusicAndVibrate(context, alarmEvent);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(alarmEvent.getFatherE().getTitle())
                .setMessage(alarmEvent.getFatherE().getTitle())
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

        AlertDialog dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    /**
     * 播放音乐
     *
     * @param context
     */
    private void playMusicAndVibrate(Context context, AlarmEvent alarmEvent) {
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
