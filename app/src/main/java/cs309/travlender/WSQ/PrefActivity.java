package cs309.travlender.WSQ;

import android.content.*;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;

import butterknife.OnClick;
import cs309.travelender.R;
import cs309.travlender.Tools.MyContext;
import cs309.travlender.Tools.PrefManager;
import cs309.travlender.Tools.Preferences;


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

