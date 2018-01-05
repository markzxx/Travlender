package cs309.travlender.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
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
import cs309.travlender.Activity.AddEventActivity;
import cs309.travlender.Activity.ViewEventActivity;
import cs309.travlender.Tools.DensityUtils;
import cs309.travlender.Tools.Event;
import cs309.travlender.Tools.EventManager;
import cs309.travlender.Activity.PrefActivity;
import cs309.travlender.Activity.AboutUs;

public class MainActivity extends Activity implements CalendarViewFragment.OnFrgDataListener,RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    private EventManager EM;
    private List<Event> eventList;
    private SwipeMenuListView Events;
    private TextView title;
    private EventAdapter adapter;
    private CalendarViewFragment calendarViewFragment;
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

    //侧边栏布局
    @Bind(R.id.main_draw_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.left_draw)
    NavigationView mNavigationView;
    //悬浮按钮
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

    //left_menu点击打开关闭侧边栏
    @OnClick(R.id.left_menu)
    void openLeftDrawe() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EM = EventManager.getInstence();
        EM.update();
        Events= (SwipeMenuListView) findViewById(R.id.event_list);
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
        init_swipe();
        init_navigation_select();

    }
    private void init_navigation_select(){
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.preference:
                        Intent intent = new Intent(MainActivity.this, PrefActivity.class);
                        startActivityForResult(intent,0);
                        break;
//                    case R.id.setting:
//                        System.out.println("setting clicked!");
//                        break;
//                    case R.id.mode:
//                        System.out.println("mode clicked!");
//                        break;
                    case R.id.aboutMe:
                        System.out.println("aboutme clicked!");
                        Intent about_me_intent=new Intent(MainActivity.this,AboutUs.class);
                        startActivity(about_me_intent);
                        break;
                }
                item.setChecked(true);//点击了设置为选中状态
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }
    private void init_swipe() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(R.color.secondary_text);
                // set item width
                deleteItem.setWidth(DensityUtils.dp2px(menu.getContext(),90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete_white_48px);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        Events.setMenuCreator(creator);
        Events.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Event event = eventList.get(position);
                switch (index) {
                    case 0:
                        // delete
                        EM.deleteEvent(event.getEventId());
                        adapter.notifyDataSetChanged();
                        recreate();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
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
                Intent intent=new Intent(MainActivity.this,ViewEventActivity.class);
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
                .setLabel("Add Event")
                .setResId(R.drawable.ic_alarm_white_48px)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setLabelSizeSp(14)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Delete")
                .setResId(R.drawable.ic_delete_white_48px)
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
            Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
            intent.putExtra("request", "ADD");
            startActivityForResult(intent,0);
        }
        if (i == 1) {
            EM.deleteAllEvent();
            recreate();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onCreate(null);
    }
}
