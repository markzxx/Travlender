package cs309.travlender.ZXX;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cs309.travelender.R;

public class SearchActivity extends Activity implements View.OnClickListener{
    private EditText text,starttime,endtime;
    private Button btnSearch;
    private EventManager EM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        text= (EditText) findViewById(R.id.search_title);
        starttime= (EditText) findViewById(R.id.search_starttime);
        endtime= (EditText) findViewById(R.id.search_endtime);
        btnSearch= (Button) findViewById(R.id.btn_search_dialog);
        btnSearch.setOnClickListener(this);
        EM = new EventManager(this);
        text.setText("test");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd");
        starttime.setText(df.format(c.getTime()));
        c.add(Calendar.DATE,1);
        endtime.setText(df.format(c.getTime()));
    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.btn_search_dialog:
                Intent i = new Intent(this,SearchResultActivity.class);
                String start=starttime.getText().toString()+" 00:00:00";
                String end=endtime.getText().toString()+" 00:00:00";
                EM.searchEvents(start,end);
                finish();
                startActivity(i);
                break;
        }
    }
}
