package cs309.travlender.Activity;

import android.content.*;
import android.os.Bundle;
import android.preference.PreferenceActivity;


/**
 * Created by alicewu on 12/31/17.
 */
public class PrefActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().add(android.R.id.content, new Settings()).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onCreate(null);
    }



}

