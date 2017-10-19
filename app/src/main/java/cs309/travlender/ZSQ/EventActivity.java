package cs309.travlender.ZSQ;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cs309.travelender.ZSQ.R;


/**
 * Created by Administrator on 2017/10/16.
 */

public class EventActivity extends Activity implements View.OnClickListener{

    private EditText etTitle,etStart,etEnd;
    private Button btnChange,btnDelete,btnAdd;
    private int id;
    private DatabaseHandler handler;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event);

        etTitle= (EditText) findViewById(R.id.event_name);
        etStart= (EditText) findViewById(R.id.event_start);
        etEnd= (EditText) findViewById(R.id.event_end);
        btnChange= (Button) findViewById(R.id.btn_change);
        btnDelete= (Button) findViewById(R.id.btn_delete);
        btnAdd= (Button) findViewById(R.id.btn_add_event);


        handler=new DatabaseHandler(this);
        //获取传递过来的intent
        intent=getIntent();

        //通过request判断，是通过那个Button点击进入的，之后隐藏或者显示相应的Button
        String request=intent.getStringExtra("request");
        switch (request){
            //点击添加按钮进入的，则只显示btnAdd
            case "Add":
                btnChange.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                btnAdd.setVisibility(View.VISIBLE);
                break;
            //通过ListView Item进入的
            case "Look":
                id=intent.getExtras().getInt("id");
                etTitle.setText(intent.getStringExtra("title"));
                etStart.setText(intent.getStringExtra("start"));
                etEnd.setText(intent.getStringExtra("end"));
                break;
        }
        btnAdd.setOnClickListener(this);
        btnChange.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_event:
                Event newEvent=new Event(id,etTitle.getText().toString(),etStart.getText().toString(),
                        etEnd.getText().toString());
                handler.addEvent(newEvent);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_change:
                Event Event=new Event(id,etTitle.getText().toString(),etStart.getText().toString(),
                        etEnd.getText().toString());
                handler.updateEvent(Event);
                //这里设置resultCode是为了区分是修改后返回主界面的还是删除后返回主界面的。
                setResult(2,intent);
                finish();
                break;
            case R.id.btn_delete:
                Event s=new Event(id,etTitle.getText().toString(),etStart.getText().toString(),
                        etEnd.getText().toString());
                handler.deleteEvent(s);
                setResult(3, intent);
                finish();
                break;
        }
    }
}
