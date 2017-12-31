package cs309.travlender.MainActivity;

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

        long getStarttime();
        void setStarttime(long start);

        long getEndtime();
        void setEndtime(long end);

        long getAddtime();
        void setAddtime(long addtime);

        long getEdittime();
        void setEdittime(long edittime);

        String getLocation();
        void setLocation(String location);

        String getTransport();
        void setTransport(String transport);



    }
}
