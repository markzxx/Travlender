package cs309.travlender.ZXX;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cs309.travelender.R;
import cs309.travlender.ZSQ.Event;

public class MainActivity extends Activity implements View.OnClickListener{
    private Button btnSearch;
    private EventManager EM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_zxx);

        EM = new EventManager(this);
        EM.addEvent(new Event("test1","100","200","300"));
        EM.addEvent(new Event("test2","300","400","500"));
        EM.addEvent(new Event("test3","600","700","800"));
        btnSearch= (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.btn_search:
                Intent i = new Intent(this,SearchActivity.class);
                startActivity(i);
                break;
        }
    }
}
