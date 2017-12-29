package cs309.travlender.ZSQ;

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

import cs309.travelender.R;
import cs309.travlender.Tools.Event;
import cs309.travlender.Tools.EventManager;


/**
 * Created by Administrator on 2017/10/16.
 */

public class EventActivity extends Activity implements View.OnClickListener{

    private EditText etTitle,etStart,etEnd,etLocation;
    private Spinner etTransport,etRemindtime;
    private Button btnChange,btnAdd;
    private int id;
    private EventManager EM;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event);

        //UI 部分
        etTitle= (EditText) findViewById(R.id.event_name);
        etStart= (EditText) findViewById(R.id.event_start);
        etEnd= (EditText) findViewById(R.id.event_end);
        etLocation= (EditText) findViewById(R.id.event_location);
        etTransport= (Spinner) findViewById(R.id.event_transport);
        etRemindtime=(Spinner) findViewById(R.id.event_remindtime);
        btnChange= (Button) findViewById(R.id.btn_change);
        btnAdd= (Button) findViewById(R.id.btn_add_event);


        EM=EventManager.getInstence();
        //获取传递过来的intent
        intent=getIntent();

        //通过request判断，是通过那个Button点击进入的，之后隐藏或者显示相应的Button
        String request=intent.getStringExtra("request");
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        switch (request){
            //点击添加按钮进入的，则只显示btnAdd
            case "Add":
                btnChange.setVisibility(View.GONE);
                Random r = new Random();
                Calendar c = Calendar.getInstance();
                etTitle.setText("test"+r.nextInt(1000));
                c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.HOUR_OF_DAY)+1,r.nextInt(60));
                etStart.setText(format.format(c.getTime()));
                c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.HOUR_OF_DAY)+1,r.nextInt(60));
                etEnd.setText(format.format(c.getTime()));
                String[] position=new String[]{"西丽劳力市场","会展中心","罗湖汽车站","深圳北站","维也纳酒店"};
                etLocation.setText(position[r.nextInt(5)]);
                etTransport.setSelection(r.nextInt(5));
                etRemindtime.setSelection(r.nextInt(5));
                btnAdd.setVisibility(View.VISIBLE);
                break;
            //通过ListView Item进入的
            case "Look":
                id=intent.getExtras().getInt("id");
                etTitle.setText(intent.getStringExtra("title"));
                etStart.setText(intent.getStringExtra("starttime"));
                etEnd.setText(intent.getStringExtra("endtime"));
                break;
        }
        btnAdd.setOnClickListener(this);
        btnChange.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_event:
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                ContentValues value = new ContentValues();
                value.put("addtime",System.currentTimeMillis());
                value.put("title",etTitle.getText().toString());
                value.put("starttime",ts.valueOf(etStart.getText().toString()+":00").getTime());
                value.put("endtime",ts.valueOf(etEnd.getText().toString()+":00").getTime());
                value.put("location",etLocation.getText().toString());
                value.put("transport",etTransport.getSelectedItem().toString());
                value.put("remindtime",getResources().getStringArray(R.array.remindvalue)[etRemindtime.getSelectedItemPosition()]);
                Event newEvent=new Event(value);
                EM.addEvent(newEvent);
                finish();
                break;
            case R.id.btn_change:
                ContentValues values = new ContentValues();
                values.put("addtime",System.currentTimeMillis());
                values.put("title",etTitle.getText().toString());
                values.put("starttime",etStart.getText().toString());
                values.put("endtime",etEnd.getText().toString());

                Event event=new Event(values);
                intent=getIntent();
                event.setEventId(intent.getIntExtra("id",0));
                EM.editEvent(event);
                //这里设置resultCode是为了区分是修改后返回主界面的还是删除后返回主界面的。
                finish();
                break;
        }

    }
}
