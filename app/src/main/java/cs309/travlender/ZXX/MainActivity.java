package cs309.travlender.ZXX;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;

import java.util.List;

import cs309.travelender.R;
import cs309.travlender.EventActivity.AddEventActivity;
import cs309.travlender.EventActivity.ViewEventActivity;
import cs309.travlender.Tools.Event;
import cs309.travlender.Tools.EventManager;
import cs309.travlender.MainActivity.EventAdapter;

public class MainActivity extends Activity implements View.OnClickListener{
    private RapidFloatingActionButton btnAdd,btnSearch,btnClear;
    private EventManager EM;
    List<Event> eventList;
    private ListView Events;
    private EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        EM = EventManager.getInstence();
        EM.update();
        Events= (ListView) findViewById(R.id.event_list);
        eventList = EM.getAllEvent();
        adapter=new EventAdapter(this,eventList);
        Events.setAdapter(adapter);
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

        btnAdd = (RapidFloatingActionButton) findViewById(R.id.fab_button_group);
        btnAdd.setOnClickListener(this);
//        btnSearch = (Button) findViewById(R.id.btn_search);
//        btnSearch.setOnClickListener(this);
        btnClear = (RapidFloatingActionButton) findViewById(R.id.fab_button_clear);
        btnClear.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        switch (view.getId()) {
//            case R.id.btn_search:
//                Intent a = new Intent(this,EventActivity.class);
//                a.putExtra("request", "Add");
//                startActivityForResult(a,1);
//                break;
            case R.id.fab_button_group:
                Intent s = new Intent(this,AddEventActivity.class);
                s.putExtra("request", "ADD");
                startActivityForResult(s,1);
                break;
            case R.id.fab_button_clear:
                EM.deleteAllEvent();
                onCreate(null);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onCreate(null);
    }
}
