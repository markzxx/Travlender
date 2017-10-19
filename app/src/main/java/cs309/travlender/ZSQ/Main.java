package cs309.travlender.ZSQ;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cs309.travelender.ZSQ.R;

public class Main extends Activity implements View.OnClickListener{

    private ListView Events;
    private EventAdapter adapter;
    private Button btnAdd,btnSearch;
    private DatabaseHandler dbHandler;
    private List<Event> EventList;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Events= (ListView) findViewById(R.id.event_list);
        btnAdd= (Button) findViewById(R.id.btn_add);
        btnSearch= (Button) findViewById(R.id.btn_search);


        btnSearch.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        dbHandler=new DatabaseHandler(this);

        //获取全部事件信息
        EventList=dbHandler.getALllEvent();
        adapter=new EventAdapter(this,EventList);
        Events.setAdapter(adapter);

        //点击ListView item跳转到详细界面
        Events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(Main.this,EventActivity.class);

                //注意这里的request是为了区分是通过什么跳转到详细界面的
                intent.putExtra("request","Look");
                intent.putExtra("id",EventList.get(i).getId());
                intent.putExtra("title",EventList.get(i).getTitle());
                intent.putExtra("start",EventList.get(i).getStart());
                intent.putExtra("end",EventList.get(i).getEnd());
                //
                startActivityForResult(intent, 0);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                Intent i=new Intent(Main.this,EventActivity.class);

                i.putExtra("request","Add");
                startActivityForResult(i, 1);
                break;
            case R.id.btn_search:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);

                //自定义View的Dialog
                final LinearLayout searchView= (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_search,null);
                builder.setView(searchView);
                final AlertDialog dialog=builder.create();
                dialog.show();

                //为自定义View的Dialog的控件添加事件监听。
                final EditText searchName= (EditText) searchView.findViewById(R.id.search_title);
                Button btnDialogSearch= (Button) searchView.findViewById(R.id.btn_search_dialog);
                btnDialogSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        searchName.setVisibility(View.GONE);
                        ListView list = (ListView) searchView.findViewById(R.id.search_result);
                        List<Event> resultList = new ArrayList<Event>();
                        final Event searchEvent = dbHandler.getEvent(searchName.getText().toString());
                        if (searchEvent != null) {
                            resultList.add(searchEvent);
                            EventAdapter resultAdapter = new EventAdapter(getApplicationContext(), resultList);
                            list.setAdapter(resultAdapter);
                            list.setVisibility(View.VISIBLE);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(Main.this, EventAdapter.class);
                                    intent.putExtra("request", "Look");
                                    intent.putExtra("id", searchEvent.getId());
                                    intent.putExtra("title",searchEvent.getTitle());
                                    intent.putExtra("start",searchEvent.getStart());
                                    intent.putExtra("end",searchEvent.getEnd());
                                    startActivityForResult(intent, 0);
                                }
                            });
                        } else {
                            //关闭Dialog
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "No Such Event!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
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
