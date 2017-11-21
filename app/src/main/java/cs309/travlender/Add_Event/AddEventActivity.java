package cs309.travlender.Add_Event;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cs309.travelender.R;
import cs309.travlender.ZSQ.Event;
import cs309.travlender.ZXX.EventManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cs309.travlender.ZXX.MainActivity;

public class AddEventActivity extends AppCompatActivity {
    private DatePickerDialog mDataPicker;
    private TimePickerDialog mStartTimePicker, mEndTimePicker;
    private boolean isAllDay = false;
    private boolean isVibrate = false;
    private EventManager EM = new EventManager(this);
    private Event event;
    private ContentValues values = new ContentValues();
    private int id;

    @BindView(R.id.alarm_title)
    EditText alarm_title;
    @BindView(R.id.alarm_description)
    EditText alarm_description;
    @BindView(R.id.alarm_replay)
    TextView alarm_replay;
    @BindView((R.id.alarm_remind))
    TextView alarm_remind;
    @BindView(R.id.alarm_local)
    TextView alarm_local;
    @BindView(R.id.alarm_color)
    TextView alarm_color;
    @BindView(R.id.alarm_tone_Path)
    TextView alarm_tone_Path;
    @BindView(R.id.alarm_date)
    TextView alarm_date;
    @BindView(R.id.insert_update_title)
    TextView insert_update_title;
    @BindView(R.id.action_bar)
    LinearLayout action_bar;

    @OnClick(R.id.alarm_date)
    void openDatePicker() {
        getDatePickerDialog();
        mDataPicker.show();
    }

    @BindView(R.id.alarm_start_time)
    TextView alarm_start_time;

    @OnClick(R.id.alarm_start_time)
    void openStartTimePicker() {
        getStartTimePickerDialog();
        mStartTimePicker.show();
    }

    @BindView(R.id.alarm_end_time)
    TextView alarm_end_time;

    @OnClick(R.id.alarm_end_time)
    void openEndTimePicker() {
        getEndTimePickerDialog();
        mEndTimePicker.show();
    }

    @OnClick(R.id.left_clear)
    void clear() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

//    @OnClick(R.id.layout_alarm_replay)
//    void openSetReplayActivity() {
//        startActivityForResult(new Intent(AddScheduleActivity.this, SetRePlayActivity.class), 0);
//    }

    @OnClick(R.id.layout_alarm_remind)
    void openSetAlarmTimeActivity() {
        startActivityForResult(new Intent(AddScheduleActivity.this, SetAlarmTimeActivity.class), 1);
    }

    @OnClick(R.id.layout_alarm_local)
    void openSetLocalActivity() {
        startActivityForResult(new Intent(AddScheduleActivity.this, SetLocalActivity.class), 2);
    }

    @OnClick(R.id.layout_alarm_color)
    void openSetColorActivity() {
        startActivityForResult(new Intent(AddScheduleActivity.this, SetColorActivity.class), 3);
    }

    @OnClick(R.id.layout_alarm_tone_Path)
    void openSetAlarmToneActivity() {
        startActivityForResult(new Intent(AddScheduleActivity.this, SetAlarmToneActivity.class), 4);
    }

    @BindView(R.id.sw_all_day)
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

    @BindView(R.id.sw_vibrate)
    Switch sw_vibrate;

    @OnClick(R.id.sw_vibrate)
    void is_Vibrate() {
        Vibrator vibrator;
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (!isVibrate) {
            alarmBean.setIsVibrate(1);
            vibrator.vibrate(500);
            isVibrate = true;
        } else {
            alarmBean.setIsVibrate(0);
            vibrator.cancel();
            isVibrate = false;
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
                alarm_date.setText(df.format(calendar.getTime()));

                //设置选择的年、月、日
                alarmBean.setYear(year);
                alarmBean.setMonth(monthOfYear);
                alarmBean.setDay(dayOfMonth);
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
                alarmBean.setStartTimeHour(hourOfDay);
                alarmBean.setStartTimeMinute(minute);
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
                alarmBean.setEndTimeHour(hourOfDay);
                alarmBean.setEndTimeMinute(minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
    }

    private Calendar getToDay() {
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(System.currentTimeMillis());
        return today;
    }
}
