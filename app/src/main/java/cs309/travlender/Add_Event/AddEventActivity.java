package cs309.travlender.Add_Event;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cs309.travelender.R;
import cs309.travlender.WHL.LocationActivity;
import cs309.travlender.ZSQ.Event;
import cs309.travlender.ZXX.EventManager;

public class AddEventActivity extends AppCompatActivity{
    private DatePickerDialog mDataPicker;
    private TimePickerDialog mTimePicker;
    private boolean isAllDay = false;
    private boolean isSmartRemind = true;
    private EventManager eventManager = new EventManager(this);
    private Event event;
    private ContentValues values = new ContentValues();
    private int id;
    private Intent intent;
    String request;

    @Bind(R.id.event_title)
    EditText event_title;
    @Bind(R.id.event_content)
    EditText event_content;
    @Bind(R.id.event_location)
    TextView event_location;
    @Bind(R.id.event_remindtime)
    Spinner event_remindtime;
    @Bind(R.id.event_transport)
    Spinner event_transport;
    @Bind(R.id.insert_update_title)
    TextView insert_update_title;
    @Bind(R.id.action_bar)
    LinearLayout action_bar;


    @Bind(R.id.start_date)
    TextView start_date;
    @OnClick(R.id.start_date)
    void openStartDatePicker() {
        getDatePickerDialog(start_date);
        mDataPicker.show();
    }

    @Bind(R.id.end_date)
    TextView end_date;
    @OnClick(R.id.end_date)
    void openEndDatePicker() {
        getDatePickerDialog(end_date);
        mDataPicker.show();
    }

    @Bind(R.id.start_time)
    TextView start_time;
    @OnClick(R.id.start_time)
    void openStartTimePicker() {
        getTimePickerDialog(start_time);
        mTimePicker.show();
    }

    @Bind(R.id.end_time)
    TextView end_time;
    @OnClick(R.id.end_time)
    void openEndTimePicker() {
        getTimePickerDialog(end_time);
        mTimePicker.show();
    }

    @OnClick(R.id.left_clear)
    void clear() {
        finish();
    }

    @OnClick(R.id.layout_location)
    void openSetLocalActivity() {
        startActivityForResult(new Intent(AddEventActivity.this, LocationActivity.class), 2);
    }

    @Bind(R.id.layout_smart_transport)
    RelativeLayout smart_tarnsport;
    @Bind(R.id.sw_smart_remind)
    Switch sw_smaart_remind;
    @OnClick(R.id.sw_smart_remind)
    void smart_remind(){
        if(!isSmartRemind)
        {
            isSmartRemind = true;
            smart_tarnsport.setVisibility(View.VISIBLE);
        }
        else
        {
            isSmartRemind = false;
            smart_tarnsport.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.sw_all_day)
    void allday() {
        if (!isAllDay) {
            start_time.setVisibility(View.GONE);
            end_time.setVisibility(View.GONE);
            isAllDay = true;
        } else {
            start_time.setVisibility(View.VISIBLE);
            end_time.setVisibility(View.VISIBLE);
            isAllDay = false;
        }
    }

    @OnClick(R.id.event_save)
    void save(){
        if(event_title.getText().toString().equals(""))
            event.setTitle("无标题");
        else
            event.setTitle(event_title.getText().toString());

        String start = start_date.getText().toString().substring(0,10); //截取 "YYYY-MM-DD"
        String end = end_date.getText().toString().substring(0,10);
        if(isAllDay){
            start += " 00:00:00";
            end += " 23:59:59";
        }else{
            start += " "+start_time.getText().toString()+":00";
            end += " "+end_time.getText().toString()+":00";
        }
        event.setStarttime(Timestamp.valueOf(start).getTime());
        event.setEndtime(Timestamp.valueOf(end).getTime());

        event.setRemindtime(getResources().getStringArray(R.array.remindvalue)[event_remindtime.getSelectedItemPosition()]);

        if(event_location.getText().toString().equals(""))
            event.setLocation("无");
        else
            event.setLocation(event_location.getText().toString());

        event.setTransport(event_transport.getSelectedItem().toString());

        if(event_content.getText().toString().equals(""))
            event.setContent("无");
        else
            event.setContent(event_content.getText().toString());

        if(isSmartRemind)
            event.setSmartRemind(1);
        else
            event.setSmartRemind(0);

        if(request.equals("ADD")){
            event.setAddtime(System.currentTimeMillis());
            eventManager.addEvent(event);
        }else if(request.equals("EDIT")){
            event.setEdittime(System.currentTimeMillis());
            eventManager.editEvent(event);
        }
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.bind(this);

        //获取传递过来的intent
        intent=getIntent();
        //通过request判断,当前是新建还是修改事件
        if(intent != null)
            request=intent.getStringExtra("request");
        else
            request = "ADD";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  EE");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:MM");
        switch (request){
            case "ADD":
                insert_update_title.setText("新建活动");
                Calendar today = getToDay();
                start_date.setText(dateFormat.format(today.getTime()));
                end_date.setText(dateFormat.format(today.getTime()));
                today.add(Calendar.HOUR_OF_DAY,+1);
                start_time.setText(timeFormat.format(today.getTime()));
                today.add(Calendar.HOUR_OF_DAY,+1);
                end_time.setText(timeFormat.format(today.getTime()));
                sw_smaart_remind.setChecked(true);
                event = new Event();
                break;
            case "EDIT":
                insert_update_title.setText("编辑活动");
                id=intent.getIntExtra("id",1);
                event = eventManager.getEvent(id);
                event_title.setText(event.getTitle());
                start_date.setText(dateFormat.format(event.getStarttime()));
                end_date.setText(dateFormat.format(event.getEndtime()));
                start_time.setText(timeFormat.format(event.getStarttime()));
                end_time.setText(timeFormat.format(event.getEndtime()));
                event_location.setText(event.getLocation());
                sw_smaart_remind.setChecked(event.getSmartRemind()==1?true:false);
                event_content.setText(event.getContent());
                event_remindtime.setSelection(Arrays.asList(getResources().getStringArray(R.array.remindvalue)).indexOf(event.getRemindtime()));
                event_transport.setSelection(Arrays.asList(getResources().getStringArray(R.array.transport)).indexOf(event.getTransport()));
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==2 && resultCode==2){
            Bundle bundle = data.getExtras();
            event_location.setText(bundle.getString("location_name","无"));
            event.setLongitude(bundle.getDouble("to_Longitude",0));
            event.setLatitude(bundle.getDouble("to_Latitude",0));
        }
    }

    /**
     * 获取日期选择器
     */
    private void getDatePickerDialog(final TextView date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        mDataPicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd  EE");
                date.setText(df.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 获取开始时间选择器
     */
    private void getTimePickerDialog(final TextView time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                time.setText(df.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
    }

    private Calendar getToDay() {
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(System.currentTimeMillis());
        return today;
    }

}
