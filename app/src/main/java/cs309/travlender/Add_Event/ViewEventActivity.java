package cs309.travlender.Add_Event;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import cs309.travelender.R;
import cs309.travlender.ZSQ.Event;
import cs309.travlender.ZXX.EventManager;

/**
 * Created by Jeremy Zhang PC on 2017/11/21.
 */

public class ViewEventActivity extends Activity implements View.OnClickListener{

    private TextView etTitle,etStart,etEnd,etLocation,etTransport,etRemindtime;
    private ImageButton btnBack,btnDele;
    private FloatingActionButton btnChange;
    private int id;
    private EventManager EM;
    private Intent intent;
    private Event currentEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_detail);

        //UI 部分
        etTitle= (TextView) findViewById(R.id.detail_alarm_title);
        etStart= (TextView) findViewById(R.id.detail_start_time);
        etEnd= (TextView) findViewById(R.id.detail_end_time);
        etRemindtime=(TextView) findViewById(R.id.detail_alarm_remind);
        etLocation= (TextView) findViewById(R.id.detail_alarm_local);
        etTransport= (TextView) findViewById(R.id.detail_alarm_transport);
        btnChange= (FloatingActionButton) findViewById(R.id.update_fab);
        btnDele= (ImageButton) findViewById(R.id.tv_delete);
        btnBack= (ImageButton) findViewById(R.id.left_alarm_back);


        EM=new EventManager(this);
        //获取传递过来的intent
        intent=getIntent();

        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm");

        id=intent.getExtras().getInt("id");
        currentEvent = EM.openEvent(id);
        etTitle.setText(currentEvent.getTitle());
        etStart.setText(format.format(new Timestamp(currentEvent.getStarttime())));
        etEnd.setText(format.format(new Timestamp(currentEvent.getEndtime())));
        etRemindtime.setText(String.valueOf(currentEvent.getRemindtime())+"分钟");
        etLocation.setText(currentEvent.getLocation());
        etTransport.setText(currentEvent.getTransport());

        btnBack.setOnClickListener(this);
        btnDele.setOnClickListener(this);
        btnChange.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_delete:
                EM.deleteEvent(id);
                finish();
                break;
            case R.id.left_alarm_back:
                finish();
                break;
            case R.id.update_fab:
                Intent intent=new Intent(ViewEventActivity.this,AddEventActivity.class);
                intent.putExtra("request","EDIT");
                intent.putExtra("id",currentEvent.getEventId());
                startActivityForResult(intent, 0);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onCreate(null);
    }
}
