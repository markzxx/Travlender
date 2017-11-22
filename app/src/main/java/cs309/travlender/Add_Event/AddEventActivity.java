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
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;
import cs309.travelender.R;
import cs309.travlender.ZSQ.Event;
import cs309.travlender.ZXX.EventManager;
import cs309.travlender.ZXX.MainActivity;

public class AddEventActivity extends AppCompatActivity {
    private DatePickerDialog mDataPicker;
    private TimePickerDialog mStartTimePicker, mEndTimePicker;
    private boolean isAllDay = false;
    private EventManager EM = new EventManager(this);
    private Event event;
    private ContentValues values = new ContentValues();
    private int id;

    @Bind(R.id.alarm_title)
    EditText alarm_title;
    @Bind(R.id.alarm_description)
    EditText alarm_description;
    @Bind(R.id.alarm_local)
    TextView alarm_local;
    @Bind(R.id.start_date)
    TextView start_date;
    @Bind(R.id.end_date)
    TextView end_date;
    @Bind(R.id.insert_update_title)
    TextView insert_update_title;
    @Bind(R.id.action_bar)
    LinearLayout action_bar;
    @Bind(R.id.event_transport)
    Spinner event_transport;

    @OnClick(R.id.start_date)
    void openDatePicker() {
        getDatePickerDialog();
        mDataPicker.show();
    }

    @Bind(R.id.start_time)
    TextView alarm_start_time;

    @OnClick(R.id.start_time)
    void openStartTimePicker() {
        getStartTimePickerDialog();
        mStartTimePicker.show();
    }

    @Bind(R.id.end_time)
    TextView alarm_end_time;

    @OnClick(R.id.end_time)
    void openEndTimePicker() {
        getEndTimePickerDialog();
        mEndTimePicker.show();
    }

    @OnClick(R.id.left_clear)
    void clear() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @OnClick(R.id.layout_alarm_local)
    void openSetLocalActivity() {
        startActivityForResult(new Intent(AddEventActivity.this, SetLocalActivity.class), 2);
    }


    @Bind(R.id.sw_all_day)
    Switch sw_all_day;

    @OnClick(R.id.sw_all_day)
    void allday() {
        if (!isAllDay) {
            alarm_start_time.setVisibility(View.GONE);
            alarm_end_time.setVisibility(View.GONE);
            isAllDay = true;
        } else {
            alarm_start_time.setVisibility(View.VISIBLE);
            alarm_end_time.setVisibility(View.VISIBLE);
            isAllDay = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
    }

    /**
     * 获取日期选择器
     */
    private void getDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        mDataPicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日  EE");
                start_date.setText(df.format(calendar.getTime()));

                //设置选择的年、月、日
//                alarmBean.setYear(year);
//                alarmBean.setMonth(monthOfYear);
//                alarmBean.setDay(dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 获取开始时间选择器
     */
    private void getStartTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        mStartTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");

                alarm_start_time.setText("开始时间:  " + df.format(calendar.getTime()));

                //设置开始时间的小时、分钟
//                alarmBean.setStartTimeHour(hourOfDay);
//                alarmBean.setStartTimeMinute(minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
    }

    /**
     * 获取结束时间选择器
     */
    private void getEndTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        mEndTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");

                alarm_end_time.setText("结束时间:  " + df.format(calendar.getTime()));


                //设置结束时间的小时、分钟
//                alarmBean.setEndTimeHour(hourOfDay);
//                alarmBean.setEndTimeMinute(minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
    }

    private Calendar getToDay() {
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(System.currentTimeMillis());
        return today;
    }
}
