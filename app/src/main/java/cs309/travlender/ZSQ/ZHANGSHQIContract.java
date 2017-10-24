package cs309.travlender.ZSQ;

import android.content.ContentValues;

/**
 * Created by Administrator on 2017/10/24.
 */

public interface ZHANGSHQIContract {

    interface EventInterface {

        ContentValues getValue();

        void setValue(ContentValues value);

        int getId();

        void setId(int id);

        String getTitle();

        void setTitle(String title);

        String getStart();

        void setStart(String start);

        String getEnd();

        void setEnd(String end);
    }
}
