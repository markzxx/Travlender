package cs309.travlender.ZXX;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cs309.travelender.R;

public class SearchActivity extends Activity implements View.OnClickListener{
    private EditText text;
    private Button btnSearch;
    private EventManager EM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        text= (EditText) findViewById(R.id.search_title);
        btnSearch= (Button) findViewById(R.id.btn_search_dialog);
        btnSearch.setOnClickListener(this);
        EM = new EventManager(this);
        text.setText("test");
    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.btn_search_dialog:
                Intent i = new Intent(this,SearchResultActivity.class);
                EM.searchEvent(text.getText().toString());
                startActivity(i);
                break;
        }
    }
}
