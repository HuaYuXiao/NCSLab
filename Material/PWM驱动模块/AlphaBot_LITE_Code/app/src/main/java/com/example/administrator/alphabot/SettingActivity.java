package com.example.administrator.alphabot;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class SettingActivity extends PreferenceActivity {

    //数据保证相关
    SharedPreferences sharedPreferences_setting;

    //发送相关


    String mForwardDown = "Forward";
    String mBackwardDown = "Backward";
    String mTurnLeftDown = "TurnLeft";
    String mTurnRightDown = "TurnRight";
    String mUpDown = "Up";
    String mDownDown = "Down";
    String mLeftDown = "Left";
    String mRightDown = "Right";
    String mForwardUp = "Stop";
    String mBackwardUp = "Stop";
    String mTurnLeftUp = "Stop";
    String mTurnRightUp = "Stop";
    String mUpUp = "Stop";
    String mDownUp = "Stop";
    String mLeftUp = "Stop";
    String mRightUp = "Stop";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //在Activity中设置ActionBar
        getActionBar().setTitle(R.string.actionBar_config);

        //得到preference对像
        sharedPreferences_setting = PreferenceManager.getDefaultSharedPreferences(this);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragement()).commit();
    }

    public static class PrefsFragement extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.config_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_reset:
                Log.i("saber","config_menu");

                //编辑
                SharedPreferences.Editor editor = sharedPreferences_setting.edit();
                editor.putString("SettingForwardDown",mForwardDown);
                editor.putString("SettingForwardUp",mForwardUp);

                editor.putString("SettingBackwardDown",mBackwardDown);
                editor.putString("SettingBackwardUp",mBackwardUp);

                editor.putString("SettingTurnLeftDown",mTurnLeftDown);
                editor.putString("SettingTurnLeftUp",mTurnLeftUp);

                editor.putString("SettingTurnRightDown",mTurnRightDown);
                editor.putString("SettingTurnRightUp",mTurnRightUp);

                editor.putString("SettingUpDown",mUpDown);
                editor.putString("SettingUpUp",mUpUp);

                editor.putString("SettingDownDown",mDownDown);
                editor.putString("SettingDownUp",mDownUp);

                editor.putString("SettingLeftDown",mLeftDown);
                editor.putString("SettingLeftUp",mLeftUp);

                editor.putString("SettingRightDown",mRightDown);
                editor.putString("SettingRightUp",mRightUp);



                //保存
                editor.commit();
                Toast.makeText(SettingActivity.this,R.string.reset_success,Toast.LENGTH_SHORT).show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
