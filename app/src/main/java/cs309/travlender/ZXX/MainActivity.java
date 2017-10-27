package cs309.travlender.ZXX;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;
import java.util.Random;

import cs309.travelender.R;
import cs309.travlender.ZSQ.Event;
import cs309.travlender.ZSQ.EventActivity;
import cs309.travlender.ZSQ.EventAdapter;
import cs309.travlender.ZSQ.Main;

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
     //   Random random = new Random();
    //    EM.addEvent(new Event("test"+random.nextInt(1000),random.nextInt(1000)+"",random.nextInt(1000)+"",random.nextInt(1000)+""));

        Events= (ListView) findViewById(R.id.event_list);
        eventList = EM.getAllEvent();
        adapter=new EventAdapter(this,eventList);
        Events.setAdapter(adapter);
        //点击ListView item跳转到详细界面
        Events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MainActivity.this,EventActivity.class);
                //注意这里的request是为了区分是通过什么跳转到详细界面的
                intent.putExtra("request","Look");
                intent.putExtra("id",eventList.get(i).getId());
                ContentValues values = eventList.get(i).getValue();
                for(String key:values.keySet()){
                    intent.putExtra(key,(String)values.get(key));
                }
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
            case R.id.btn_add:
                Intent a = new Intent(this,EventActivity.class);
                a.putExtra("request", "Add");
                startActivity(a);
                break;
            case R.id.btn_search:
                Intent s = new Intent(this,SearchActivity.class);
                startActivity(s);
                break;
            case R.id.btn_clear:
                EM.deleteAllEvent();
                Intent d = new Intent(this,MainActivity.class);
                startActivity(d);
        }
    }
}
