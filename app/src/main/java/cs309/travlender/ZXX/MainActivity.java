package cs309.travlender.ZXX;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import cs309.travelender.R;
import cs309.travlender.Add_Event.AddEventActivity;
import cs309.travlender.Add_Event.ViewEventActivity;
import cs309.travlender.ZSQ.Event;
import cs309.travlender.ZSQ.EventActivity;
import cs309.travlender.ZSQ.EventAdapter;

public class MainActivity extends Activity implements View.OnClickListener{
    private Button btnAdd,btnSearch,btnClear;
    private EventManager EM;
    List<Event> eventList;
    private ListView Events;
    private EventAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_zxx);

        EM = new EventManager(this);
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

        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);
        btnClear = (Button) findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.btn_search:
                Intent a = new Intent(this,EventActivity.class);
                a.putExtra("request", "Add");
                startActivityForResult(a,1);
                break;
            case R.id.btn_add:
                Intent s = new Intent(this,AddEventActivity.class);
                s.putExtra("request", "ADD");
                startActivityForResult(s,1);
                break;
            case R.id.btn_clear:
                EM.deleteAllEvent();
                onCreate(null);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onCreate(null);
    }
}
