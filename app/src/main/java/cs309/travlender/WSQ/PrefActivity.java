package cs309.travlender.WSQ;

import android.app.Activity;
import android.content.*;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.OnClick;
import cs309.travelender.R;

/**
 * Created by alicewu on 12/31/17.
 */
public class PrefActivity extends PreferenceActivity{

    @OnClick(R.id.back_pref)
    void back(){
        finish();
    }
    @OnClick(R.id.pref_save)
    void save(){

        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().add(android.R.id.content, new Settings()).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onCreate(null);
    }


    class Settings extends PreferenceFragment{
        SharedPreferences.OnSharedPreferenceChangeListener mChangeListener;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    //监听到活动后主要在这里调用方法修改数据库
//                if ("pop-up_win".equals(key) || "notifications_vibrate".equals(key) || "auto_plan".equals(key)) {
//                    Toast.makeText(mActivity, key + " : change to " + sharedPreferences.getBoolean(key, true), Toast.LENGTH_SHORT).show();
//                    if("pop-up_win".equals(key)){
//                        setDB.isPopWin(sharedPreferences.getBoolean(key, true));
//                    }
//                    else if ("notifications_vibrate".equals(key)){
//                        setDB.isVibrate(sharedPreferences.getBoolean(key, true));
//                    }
//                    else {
//                        setDB.isAutoPlan(sharedPreferences.getBoolean(key, true));
//                    }
//                } else if ("remind_before".equals(key)) {
//                    setDB.remind_before(sharedPreferences.getString(key, "Never"));
//                    findPreference("remind_before").setSummary("remind event " + sharedPreferences.getString(key, "Never") + " before");
//                } else if ("notifications_ringtone".equals(key)) {
//                    setDB.modifyRingtone(sharedPreferences.getString(key, "Default ring"));//default ring应修改为系统铃声引用，此处不应为字符串
//                    findPreference("notifications_ringtone").setSummary(sharedPreferences.getString(key, "Default ring"));
                }
//            }
            };
            addPreferencesFromResource(R.xml.preference);
            setContentView(R.layout.pref_layout);
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

}

