package cs309.travlender.Tools;

import android.app.Application;
import android.content.Context;
/**
 * Created by markz on 2017-12-25.
 */

public class MyContext extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
