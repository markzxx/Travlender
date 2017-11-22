package cs309.travlender.Add_Event;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import cs309.travelender.R;
import cs309.travlender.ZSQ.Event;
import cs309.travlender.ZXX.EventManager;

/**
 * Created by Jeremy Zhang PC on 2017/11/21.
 */

public class ViewEventActivity extends Activity{

    private int id;
    private EventManager EM;
    private Intent intent;
    private Event currentEvent;

    @Bind(R.id.detail_alarm_title)
    TextView etTitle;
    @Bind(R.id.detail_start_time)
    TextView etStart;
    @Bind(R.id.detail_end_time)
    TextView etEnd;
    @Bind(R.id.detail_alarm_remind)
    TextView etRemindtime;
    @Bind(R.id.detail_alarm_local)
    TextView etLocation;
    @Bind(R.id.detail_alarm_transport)
    TextView etTransport;

    @Bind(R.id.tv_delete)
    ImageButton btnDele;
    @Bind(R.id.left_alarm_back)
    ImageButton btnBack;

    @OnClick(R.id.update_fab)
    void toEdit() {
        Intent intent=new Intent(ViewEventActivity.this,AddEventActivity.class);
        intent.putExtra("request","EDIT");
        intent.putExtra("id",currentEvent.getEventId());
        startActivityForResult(intent,0);
    }

    @OnClick(R.id.tv_delete)
    void toDelete() {
        EM.deleteEvent(id);
        finish();
    }

    @OnClick(R.id.left_alarm_back)
    void toBack() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_detail);
        ButterKnife.bind(this);
        EM=new EventManager(this);
        //获取传递过来的intent
        intent=getIntent();

        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm");

        id=intent.getExtras().getInt("id");
        currentEvent = EM.getEvent(id);
        //currentEvent= EM.getAllEvent().get(0);
        etTitle.setText(currentEvent.getTitle());
        etStart.setText(format.format(new Timestamp(currentEvent.getStarttime())));
        etEnd.setText(format.format(new Timestamp(currentEvent.getEndtime())));
        etRemindtime.setText(String.valueOf(currentEvent.getRemindtime())+"分钟");
        etLocation.setText(currentEvent.getLocation());
        etTransport.setText(currentEvent.getTransport());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onCreate(null);
    }

}
