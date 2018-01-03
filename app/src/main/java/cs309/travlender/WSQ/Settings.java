package cs309.travlender.WSQ;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cs309.travelender.R;
import cs309.travlender.Tools.MyContext;
import cs309.travlender.Tools.PrefManager;
import cs309.travlender.Tools.Preferences;

/**
 * Created by Administrator on 2018-01-04.
 */

public class Settings extends PreferenceFragment {
    SharedPreferences.OnSharedPreferenceChangeListener mChangeListener;
    Boolean isPopWin = true, isVibrate = true, isAutoPlan = true;
    String modifyRingtone = MyContext.getContext().getString(R.string.pref_default_ringtone),
            transport = MyContext.getContext().getString(R.string.pref_default_transportation);
    int remind_before = Integer.parseInt(MyContext.getContext().getString(R.string.pref_default_remind_before));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                //监听到活动后主要在这里调用方法修改数据库
                if ("pop-up_win".equals(key) || "notifications_vibrate".equals(key) || "auto_plan".equals(key)) {
                    if ("pop-up_win".equals(key)) {
                        isPopWin = sharedPreferences.getBoolean(key, true);
                    } else if ("notifications_vibrate".equals(key)) {
                        isVibrate = sharedPreferences.getBoolean(key, true);
                    } else {
                        isAutoPlan = sharedPreferences.getBoolean(key, true);
                    }
                } else if ("remind_before".equals(key)) {
                    remind_before = Integer.parseInt(sharedPreferences.getString(key, "5"));
                } else if ("notifications_ringtone".equals(key)) {
                    modifyRingtone = sharedPreferences.getString(key, MyContext.getContext().getString(R.string.pref_default_ringtone));
                } else if ("transportation".equals(key))
                    transport = sharedPreferences.getString(key, MyContext.getContext().getString(R.string.pref_default_transportation));
            }
        };
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
        Preferences pref = new Preferences();
        pref.setIsAutoplan(isAutoPlan == true ? 1 : 0);
        pref.setIsPopwin(isPopWin == true ? 1 : 0);
        pref.setIsVibrate(isVibrate == true ? 1 : 0);
        pref.setRemindBefore(remind_before);
        pref.setTransport(transport);
        pref.setRingtone(modifyRingtone);
        PrefManager.getInstence().editPref(pref);
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mChangeListener);

    }

    //将value绑定到summary
    private void bindPreferenceSummaryToValue(Preference preference) {
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
    private Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
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
