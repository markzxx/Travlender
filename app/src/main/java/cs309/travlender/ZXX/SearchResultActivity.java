package cs309.travlender.ZXX;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.List;

import cs309.travelender.R;
import cs309.travlender.ZSQ.Event;
import cs309.travlender.ZSQ.EventAdapter;

public class SearchResultActivity extends AppCompatActivity {
    List<Event> searchResult;
    private ListView Events;
    private EventAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        searchResult = new EventManager(this).SearchList;
        adapter=new EventAdapter(this,searchResult);
        Events.setAdapter(adapter);
    }
}
