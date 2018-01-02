package cs309.travlender.WSQ;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import cs309.travelender.R;

/**
 * Created by alicewu on 12/31/17.
 */

public class Settings extends PreferenceFragment {
    SharedPreferences.OnSharedPreferenceChangeListener mChangeListener;
    Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
        mChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Toast.makeText(mActivity, key + " : change to " + sharedPreferences.getBoolean(key, true), Toast.LENGTH_SHORT).show();
//                if ("setting_wifi".equals(key) || "setting_bluetouh".equals(key) || "charge_lock_screen".equals(key) || "never_sleep".equals(key)) {
//                    Toast.makeText(mActivity, key + " : change to " + sharedPreferences.getBoolean(key, true), Toast.LENGTH_SHORT).show();
//                } else if ("setting_timezone".equals(key)) {
//                    findPreference("setting_timezone").setSummary(sharedPreferences.getString(key, "GMY - 02:00"));
//                }
                findPreference("remind_before").setSummary(sharedPreferences.getString(key, "GMY - 02:00"));

            }
        };

        addPreferencesFromResource(R.xml.preference);
    }

    //注册活动
    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(mChangeListener);
    }

    //注销活动
    @Override
    public void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mChangeListener);
    }

}