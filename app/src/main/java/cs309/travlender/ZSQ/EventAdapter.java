package cs309.travlender.ZSQ;

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
        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.list_item_new,viewGroup,false);
        }

        TextView tvName= (TextView) view.findViewById(R.id.title);
        TextView tvStart= (TextView) view.findViewById(R.id.start);
        TextView tvEnd= (TextView) view.findViewById(R.id.end);
        TextView tvId= (TextView) view.findViewById(R.id.id);
//        TextView tvAddTime= (TextView) view.findViewById(R.id.addtime);
        TextView tvLocation= (TextView) view.findViewById(R.id.location);
//        TextView tvTransport= (TextView) view.findViewById(R.id.transport);
//        TextView tvEditTime= (TextView) view.findViewById(R.id.edittime);
//        TextView tvRemindTime= (TextView) view.findViewById(R.id.remindtime);

        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm");

        tvName.setText(""+events.get(i).getTitle());
        tvStart.setText("开始："+df.format(new Timestamp(events.get(i).getStarttime())));
        tvEnd.setText("结束："+df.format(new Timestamp(events.get(i).getEndtime())));
        tvId.setText("Id  "+events.get(i).getEventId());
//        tvAddTime.setText("Add Time  "+df.format(new Timestamp(events.get(i).getAddtime())));
        tvLocation.setText("位置:  "+events.get(i).getLocation());
//        tvTransport.setText("Transport  "+events.get(i).getTransport());
//        tvEditTime.setText("Edit Time "+df.format(new Timestamp(events.get(i).getEdittime())));
//        tvRemindTime.setText("Remind Time "+events.get(i).getRemindtime()+"分钟");

        return view;
    }
}
