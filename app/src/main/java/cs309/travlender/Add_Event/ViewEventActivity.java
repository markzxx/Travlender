package cs309.travlender.Add_Event;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import cs309.travlender.ZSQ.Event;
import cs309.travlender.ZXX.EventManager;

/**
 * Created by Jeremy Zhang PC on 2017/11/21.
 */

public class ViewEventActivity extends Activity implements View.OnClickListener{

    private EditText etTitle,etStart,etEnd,etLocation,etTransport,etRemindtime;
    private Button btnChange,btnDele,btnBack;
    private int id;
    private EventManager EM;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_detail);

        //UI 部分
        etTitle= (EditText) findViewById(R.id.detail_alarm_title);
        etStart= (EditText) findViewById(R.id.detail_start_time);
        etEnd= (EditText) findViewById(R.id.detail_end_time);
        etRemindtime=(EditText) findViewById(R.id.detail_alarm_remind);
        etLocation= (EditText) findViewById(R.id.detail_alarm_local);
        etTransport= (EditText) findViewById(R.id.detail_alarm_transport);
        btnChange= (Button) findViewById(R.id.update_fab);
        btnDele= (Button) findViewById(R.id.tv_delete);
        btnBack= (Button) findViewById(R.id.left_alarm_back);


        EM=new EventManager(this);
        //获取传递过来的intent
        intent=getIntent();

        //通过request判断，是通过那个Button点击进入的，之后隐藏或者显示相应的Button
        String request=intent.getStringExtra("request");
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        switch (request){
            //通过ListView Item进入的
            case "Look":
                id=intent.getExtras().getInt("id");
                etTitle.setText(intent.getStringExtra("title"));
                etStart.setText(intent.getStringExtra("starttime"));
                etEnd.setText(intent.getStringExtra("endtime"));
                etRemindtime.setText(intent.getStringExtra("remindtime"));
                etLocation.setText(intent.getStringExtra("location"));
                etTransport.setText(intent.getStringExtra("transport"));
                break;
        }
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
        }

    }
}
