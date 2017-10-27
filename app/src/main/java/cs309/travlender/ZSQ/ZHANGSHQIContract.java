package cs309.travlender.ZSQ;

import android.content.ContentValues;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface ZHANGSHQIContract {

    interface EventInterface {

        ContentValues getValue();
        void setValue(ContentValues value);

        int getEventId();
        void setEventId(int id);

        String getTitle();
        void setTitle(String title);

        String getStarttime();
        void setStarttime(String start);

        String getEndtime();
        void setEndtime(String end);

        String getAddtime();
        void setAddtime(String addtime);

        String getEdittime();
        void setEdittime(String edittime);

        String getLocation();
        void setLocation(String location);

        String getTransport();
        void setTransport(String transport);



    }
}
