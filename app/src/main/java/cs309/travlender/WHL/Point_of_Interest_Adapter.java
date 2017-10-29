package cs309.travlender.WHL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cs309.travelender.R;

/**
 * Created by Dell on 2017/10/28.
 */

class Point_of_Interest_Adapter extends ArrayAdapter<Point_of_Interest>{

    private int resourceId;

    public Point_of_Interest_Adapter(Context context, int textViewResourceId, List<Point_of_Interest> obj){
        super(context, textViewResourceId, obj);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Point_of_Interest poi = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.logo = (ImageView) view.findViewById(R.id.item_logo);
            viewHolder.name = (TextView) view.findViewById(R.id.item_name);
            viewHolder.subname = (TextView) view.findViewById(R.id.item_subname);
            viewHolder.distance = (TextView) view.findViewById(R.id.distance);
            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.logo.setImageResource(poi.getImageId());
        viewHolder.name.setText(poi.getName());
        viewHolder.subname.setText(poi.getSubname());
        return view;
    }
}

class ViewHolder{
    ImageView logo;
    TextView name;
    TextView subname;
    TextView distance;
}