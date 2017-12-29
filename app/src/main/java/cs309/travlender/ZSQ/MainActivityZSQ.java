package cs309.travlender.ZSQ;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cs309.travelender.R;
import cs309.travlender.Add_Event.AddEventActivity;
import cs309.travlender.Add_Event.ViewEventActivity;
import cs309.travlender.Tools.Event;
import cs309.travlender.Tools.EventManager;


public class MainActivityZSQ extends Activity implements CalendarViewFragment.OnFrgDataListener,RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {


    private EventManager EM;
    private List<Event> eventList;
    private ListView Events;
    private TextView title;
    private EventAdapter adapter;
    private CalendarViewFragment calendarViewFragment;
    //向dialog传递数据
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");


    @Bind(R.id.fab_layout)
    RapidFloatingActionLayout fab_layout;
    @Bind(R.id.fab_button_group)
    RapidFloatingActionButton fab_button_group;
    private RapidFloatingActionHelper rfabHelper;

    //日历按钮
    @OnClick(R.id.go_today)
    void open_calendar(){
        calendarViewFragment.show(getFragmentManager(),"calendar_layout");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        EM = EventManager.getInstence();
        Events= (ListView) findViewById(R.id.event_list);
        title = (TextView) findViewById(R.id.title_day);
        calendarViewFragment=new CalendarViewFragment();
        ButterKnife.bind(this);
        Date current;
        if (calendarViewFragment.getMcv()!=null)
            current = calendarViewFragment.getMcv().getSelectedDate().getDate();
        else
            current = new Date();
        title.setText(sdf.format(current));
        initFab();
        viewList(current);
        viewEvent();


    }

    @Override
    public void progress(Date date) {
        //每次选择日期后,根据选择的日期从数据库寻找事件添加到listview中
        if(date!=null){
            viewList(date);
            title.setText(sdf.format(date));
        }
    }

        private void viewList(Date date) {
        String str=sdf.format(date);
        eventList = EM.getEvents(str);
        adapter=new EventAdapter(this,eventList);
        Events.setAdapter(adapter);
    }


    private void viewEvent() {
        //点击ListView item跳转到详细界面
        Events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MainActivityZSQ.this,ViewEventActivity.class);
                //注意这里的request是为了区分是通过什么跳转到详细界面的
                intent.putExtra("id",eventList.get(i).getEventId());
                startActivityForResult(intent, 0);
            }
        });
    }

    /**
     * 初始化 Fab
     */
    private void initFab() {
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("活动")
                .setResId(R.drawable.ic_access_alarms_white_24dp)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setLabelSizeSp(14)
                .setWrapper(0)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(5)
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(5)
        ;
        rfabHelper = new RapidFloatingActionHelper(
                this,
                fab_layout,
                fab_button_group,
                rfaContent
        ).build();

    }

    @Override
    public void onRFACItemLabelClick(int i, RFACLabelItem rfacLabelItem) {
        Toast.makeText(this, "Click Label !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRFACItemIconClick(int i, RFACLabelItem rfacLabelItem) {
        if (i == 0) {
            Intent intent = new Intent(MainActivityZSQ.this, AddEventActivity.class);
            intent.putExtra("request", "ADD");
            startActivityForResult(intent,0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onCreate(null);
    }
}
