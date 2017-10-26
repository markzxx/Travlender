package cs309.travlender.ZSQ;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cs309.travelender.R;
import cs309.travlender.ZXX.EventManager;

public class Main extends Activity implements View.OnClickListener{

    private ListView Events;
    private EventAdapter adapter;
    private Button btnAdd,btnSearch;
    private List<Event> EventList;
    private EventManager eventManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI部分
        Events= (ListView) findViewById(R.id.event_list);
        btnAdd= (Button) findViewById(R.id.btn_add);
        btnSearch= (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        eventManager=new EventManager(this);

        //获取全部事件信息
        EventList=eventManager.getALllEvent();
        adapter=new EventAdapter(this,EventList);
        Events.setAdapter(adapter);

        //点击ListView item跳转到详细界面
        Events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(Main.this,EventActivity.class);
                //注意这里的request是为了区分是通过什么跳转到详细界面的
                intent.putExtra("request","Look");
                ContentValues values = EventList.get(i).getValue();
                for(String key:values.keySet()){
                    intent.putExtra(key,(String)values.get(key));
                }
                startActivityForResult(intent, 0);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                Intent i = new Intent(Main.this, EventActivity.class);
                i.putExtra("request", "Add");
                startActivityForResult(i, 1);
                break;
            case R.id.btn_search:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //根据返回的resultCode判断是通过哪种操作返回的，并提示相关信息；
        switch (requestCode){
            case 0:
                if (resultCode==2)
                    Toast.makeText(this,"Edited",Toast.LENGTH_SHORT).show();
                if (resultCode==3)
                    Toast.makeText(this,"Deleted",Toast.LENGTH_SHORT).show();
                break;
            case 1:
                if (resultCode==RESULT_OK)
                    Toast.makeText(this,"Added",Toast.LENGTH_SHORT).show();
                break;
        }
        /**
         * 如果这里仅仅使用adapter.notifyDataSetChanged()是不会刷新界面ListView的，
         * 因为此时adapter中传入的EventList并没有给刷新，即adapter也没有被刷新，所以你可以
         * 重新获取EventList后再改变adapter，我这里通过调用onCreate()重新刷新了整个界面
         */

        //        EventList=dbHandler.getALllEvent();
        //        adapter=new EventAdapter(this,EventList);
        //        Events.setAdapter(adapter);
        onCreate(null);
    }
}
