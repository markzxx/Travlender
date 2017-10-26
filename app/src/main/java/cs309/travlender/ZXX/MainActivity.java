package cs309.travlender.ZXX;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;
import java.util.Random;

import cs309.travelender.R;
import cs309.travlender.ZSQ.Event;
import cs309.travlender.ZSQ.EventAdapter;

public class MainActivity extends Activity implements View.OnClickListener{
    private Button btnSearch,btnClear;
    private EventManager EM;
    List<Event> eventList;
    private ListView Events;
    private EventAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_zxx);

        EM = new EventManager(this);
        Random random = new Random();
        EM.addEvent(new Event("test"+random.nextInt(1000),random.nextInt(1000)+"",random.nextInt(1000)+"",random.nextInt(1000)+""));

        Events= (ListView) findViewById(R.id.event_list);
        eventList = EM.getAllEvent();
        adapter=new EventAdapter(this,eventList);
        Events.setAdapter(adapter);

        btnSearch= (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);
        btnClear= (Button) findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.btn_search:
                Intent i = new Intent(this,SearchActivity.class);
                startActivity(i);
                break;
            case R.id.btn_clear:
                EM.deleteAllEvent();
                Intent d = new Intent(this,MainActivity.class);
                startActivity(d);
        }
    }
}
