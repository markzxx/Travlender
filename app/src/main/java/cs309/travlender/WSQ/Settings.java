package cs309.travlender.WSQ;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
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
                //监听到活动后主要在这里调用方法修改数据库
                if ("pop-up_win".equals(key) || "notifications_vibrate".equals(key) || "auto_plan".equals(key)) {
                    Toast.makeText(mActivity, key + " : change to " + sharedPreferences.getBoolean(key, true), Toast.LENGTH_SHORT).show();
                    if ("pop-up_win".equals(key)) {
//                        setDB.isPopWin(sharedPreferences.getBoolean(key, true));
                    } else if ("notifications_vibrate".equals(key)) {
//                        setDB.isVibrate(sharedPreferences.getBoolean(key, true));
                    } else {
//                        setDB.isAutoPlan(sharedPreferences.getBoolean(key, true));
                    }
                } else if ("remind_before".equals(key)) {
//                    setDB.remind_before(sharedPreferences.getString(key, "Never"));
//                    findPreference("remind_before").setSummary("remind event " + sharedPreferences.getString(key, "Never") + " before");
                } else if ("notifications_ringtone".equals(key)) {
//                    setDB.modifyRingtone(sharedPreferences.getString(key, "Default ring"));//default ring应修改为系统铃声引用，此处不应为字符串
//                    findPreference("notifications_ringtone").setSummary(sharedPreferences.getString(key, "Default ring"));
                } else if ("transportation".equals(key)) {
                }

            }
        };
        //调用xml文件
        addPreferencesFromResource(R.xml.preference);
        bindPreferenceSummaryToValue(findPreference("notifications_ringtone"));
        bindPreferenceSummaryToValue(findPreference("remind_before"));
        bindPreferenceSummaryToValue(findPreference("transportation"));

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

    //将value绑定到summary
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };


}