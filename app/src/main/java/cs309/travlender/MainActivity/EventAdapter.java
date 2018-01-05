package cs309.travlender.MainActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import cs309.travelender.R;
import cs309.travlender.Tools.Event;

/**
 * Created by Administrator on 2017/10/16.
 */

public class EventAdapter extends BaseAdapter {
    private List<Event> events;
    private Context context;

    public EventAdapter(Context context, List<Event> events) {
        super();
        this.events=events;
        this.context=context;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int i) {
        return events.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Event event = events.get(i);
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);
        }
        init_info(view, event);
        return view;
    }

    private void init_info(View view, Event event) {
        TextView tvName= (TextView) view.findViewById(R.id.title);
        TextView tvStart= (TextView) view.findViewById(R.id.start);
        TextView tvEnd= (TextView) view.findViewById(R.id.end);
        TextView tvId= (TextView) view.findViewById(R.id.id);
        TextView tvLocation= (TextView) view.findViewById(R.id.location);
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");
        tvName.setText(""+event.getTitle());
        tvStart.setText("开始："+df.format(new Timestamp(event.getStarttime())));
        tvEnd.setText("结束："+df.format(new Timestamp(event.getEndtime())));
        tvId.setText("Id  "+event.getEventId());
        tvLocation.setText("位置:  "+event.getLocation());
    }


}
