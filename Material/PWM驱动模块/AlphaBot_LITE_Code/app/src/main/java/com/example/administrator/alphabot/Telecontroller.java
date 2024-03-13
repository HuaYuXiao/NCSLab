package com.example.administrator.alphabot;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class Telecontroller extends Activity {
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_CONNECT = 6;
    public static final int MESSAGE_CONNECT_SUCCEED = 7;
    public static final int MESSAGE_CONNECT_LOST = 8;
    public static final int MESSAGE_RECV = 10;
    public static final int MESSAGE_EXCEPTION_RECV = 11;

    String mDeviceName;
    String mDeviceAddress;

    private boolean mConnected = false;

    //数据保证相关
    SharedPreferences sharedPreferences_setting;

    //按键相关
    Button mButtonForward;
    Button mButtonBackward;
    Button mButtonTurnLeft;
    Button mButtonTurnRight;
    Button mButtonUp;
    Button mButtonDown;
    Button mButtonLeft;
    Button mButtonRight;

    //状态显示相关
    TextView mTextViewState;
    String mState;

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

    //蓝牙相关
    public static String EDR_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mBluetoothSocket = null;
    private InputStream mInStream;
    private OutputStream mOutStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telecontroller);


        //在Activity中设置ActionBar
        getActionBar().setTitle(R.string.actionBar_telecontroller_mode);

        //得到Activity的输入值
        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        setButton();

        setTextView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //得到BluetoothAdapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter.getState() != BluetoothAdapter.STATE_ON)
        {
            Toast.makeText(this,R.string.bluetooth_not_supported,Toast.LENGTH_SHORT).show();
            finish();
        }

        IntentFilter mIntent = new IntentFilter();
        mIntent.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        mIntent.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        registerReceiver(connectDevices, mIntent);
        mHandler.sendEmptyMessageDelayed(MESSAGE_CONNECT, 1000);
    }

    public final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_CONNECT:
                    Toast.makeText(Telecontroller.this, R.string.ble_connected_success, Toast.LENGTH_SHORT).show();
                    new Thread(new Runnable() {
                        public void run() {
                            InputStream tmpIn;
                            OutputStream tmpOut;
                            try {
                                UUID uuid = UUID.fromString(EDR_UUID);
                                BluetoothDevice btDev = mBluetoothAdapter
                                        .getRemoteDevice(mDeviceAddress);
                                mBluetoothSocket = btDev.createInsecureRfcommSocketToServiceRecord(uuid);
                                mBluetoothSocket.connect();
                                tmpIn = mBluetoothSocket.getInputStream();
                                tmpOut = mBluetoothSocket.getOutputStream();
                            } catch (Exception e) {
                                mConnected = false;
                                mInStream = null;
                                mOutStream = null;
                                mBluetoothSocket = null;
                                e.printStackTrace();
                                mHandler.sendEmptyMessage(MESSAGE_CONNECT_LOST);
                                return;
                            }
                            mInStream = tmpIn;
                            mOutStream = tmpOut;
                            mHandler.sendEmptyMessage(MESSAGE_CONNECT_SUCCEED);
                        }
                    }).start();
                    break;
                case MESSAGE_CONNECT_SUCCEED:
                    mConnected = true;
                    mButtonForward.setEnabled(true);
                    mButtonBackward.setEnabled(true);
                    mButtonTurnLeft.setEnabled(true);
                    mButtonTurnRight.setEnabled(true);
                    mButtonUp.setEnabled(true);
                    mButtonDown.setEnabled(true);
                    mButtonLeft.setEnabled(true);
                    mButtonRight.setEnabled(true);
                    new Thread(new Runnable() {
                        public void run() {
                            byte[] bufRecv = new byte[1024];
                            int nRecv = 0;
                            while (mConnected) {
                                try {
                                    nRecv = mInStream.read(bufRecv);
                                    if (nRecv < 1) {
                                        Thread.sleep(100);
                                        continue;
                                    }
                                    byte[] nPacket = new byte[nRecv];
                                    System.arraycopy(bufRecv, 0, nPacket, 0, nRecv);
                                    mHandler.obtainMessage(MESSAGE_RECV,
                                            nRecv, -1, nPacket).sendToTarget();
                                    Thread.sleep(100);
                                } catch (Exception e) {
                                    mHandler.sendEmptyMessage(MESSAGE_EXCEPTION_RECV);
                                    break;
                                }
                            }
                        }
                    }).start();
                    break;
                case MESSAGE_EXCEPTION_RECV:
                    break;
                case MESSAGE_CONNECT_LOST:
                    Toast.makeText(Telecontroller.this, R.string.ble_connected_failed, Toast.LENGTH_SHORT).show();
                    mButtonForward.setEnabled(false);
                    mButtonBackward.setEnabled(false);
                    mButtonTurnLeft.setEnabled(false);
                    mButtonTurnRight.setEnabled(false);
                    mButtonUp.setEnabled(false);
                    mButtonDown.setEnabled(false);
                    mButtonLeft.setEnabled(false);
                    mButtonRight.setEnabled(false);
                    try {
                        if (mInStream != null)
                            mInStream.close();
                        if (mOutStream != null)
                            mOutStream.close();
                        if (mBluetoothSocket != null)
                            mBluetoothSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        mInStream = null;
                        mOutStream = null;
                        mBluetoothSocket = null;
                        mConnected = false;
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:
                    break;
                case MESSAGE_RECV:
                    byte[] bBuf = (byte[]) msg.obj;
                    String data = bytesToString(bBuf, msg.arg1);
                    if (data != null)
                    {
                        if(data.lastIndexOf('{')!=-1)
                        {
                            mState="";
                            mState = mState+data;
                        }
                        else
                        {
                            mState = mState+data;
                        }

                        if (data.lastIndexOf('}')!=-1)
                        {
                            Log.i("saber","1");
                            try {
                                Log.i("saber","2");
                                JSONObject jsonObject = new JSONObject(mState);
                                String speed_str = jsonObject.getString("State");
                                mTextViewState.setText(speed_str);
                            } catch (JSONException e) {
                                Log.i("saber","3");
                                e.printStackTrace();
                            }
                        }


                        //String str = "{\"Speed\":\"High\"}";



//                        if (mDataField.length() > 5000)
//                        {
//                            mDataField.setText("");
//                        }
                        //计数相关
//                        long count = data.length();
//                        receiveDataCount += count;
//                        mReceiveDataCount.setText(R.string.receive_data);
//                        mReceiveDataCount.append(""+receiveDataCount);

//                        mDataField.append(data);
//                        svResult.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                svResult.fullScroll(ScrollView.FOCUS_DOWN);
//                            }
//                        });
                    }
                    break;
            }
        }
    };

    public static String bytesToString(byte[] b, int length) {
        StringBuffer result = new StringBuffer("");
        for (int i = 0; i < length; i++) {
            result.append((char) (b[i]));
        }

        return result.toString();
    }

    void setTextView()
    {
        mTextViewState = (TextView)findViewById(R.id.state_textView);
        mTextViewState.setText(R.string.state);
    }

    private BroadcastReceiver connectDevices = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {
            } else if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {

            }
        }
    };

    /* DEMO版较为简单，在编写您的应用时，请将此函数放到线程中执行，以免UI不响应 */
    void send(String strValue) {
        if (!mConnected)
            return;
        try {
            if (mOutStream == null)
                return;
            mOutStream.write(strValue.getBytes());
        } catch (Exception e) {
            Toast.makeText(Telecontroller.this, R.string.ble_connected_failed, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mConnected) {
                mConnected = false;

                try {
                    Thread.sleep(100);
                    if (mInStream != null)
                        mInStream.close();
                    if (mOutStream != null)
                        mOutStream.close();
                    if (mBluetoothSocket != null)
                        mBluetoothSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(connectDevices);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.setting:
                final Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setButton()
    {
        //得到preference对像
        sharedPreferences_setting = PreferenceManager.getDefaultSharedPreferences(this);

        mButtonForward = (Button) findViewById(R.id.ForwardBtn);
        mButtonForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        send(sharedPreferences_setting.getString("SettingForwardDown", mForwardDown));
                        break;
                    case MotionEvent.ACTION_UP:
                        send(sharedPreferences_setting.getString("SettingForwardUp", mForwardUp));
                        break;
                }
                return false;
            }
        });

        mButtonBackward = (Button) findViewById(R.id.BackwardBtn);
        mButtonBackward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        send(sharedPreferences_setting.getString("SettingBackwardDown", mBackwardDown));
                        break;
                    case MotionEvent.ACTION_UP:
                        send(sharedPreferences_setting.getString("SettingBackwardUp", mBackwardUp));
                        break;
                }
                return false;
            }
        });

        mButtonTurnLeft = (Button) findViewById(R.id.TurnLeftBtn);
        mButtonTurnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        send(sharedPreferences_setting.getString("SettingTurnLeftDown", mTurnLeftDown));
                        break;
                    case MotionEvent.ACTION_UP:
                        send(sharedPreferences_setting.getString("SettingTurnLeftUp", mTurnLeftUp));
                        break;
                }
                return false;
            }
        });

        mButtonTurnRight = (Button) findViewById(R.id.TurnRightBtn);
        mButtonTurnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        send(sharedPreferences_setting.getString("SettingTurnRightDown", mTurnRightDown));
                        break;
                    case MotionEvent.ACTION_UP:
                        send(sharedPreferences_setting.getString("SettingTurnRightUp", mTurnRightUp));
                        break;
                }
                return false;
            }
        });

        mButtonUp = (Button) findViewById(R.id.UpBtn);
        mButtonUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        send(sharedPreferences_setting.getString("SettingUpDown", mUpDown));
                        break;
                    case MotionEvent.ACTION_UP:
                        send(sharedPreferences_setting.getString("SettingUpUp", mUpUp));
                        break;
                }
                return false;
            }
        });

        mButtonDown = (Button) findViewById(R.id.DownBtn);
        mButtonDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        send(sharedPreferences_setting.getString("SettingDownDown", mDownDown));
                        break;
                    case MotionEvent.ACTION_UP:
                        send(sharedPreferences_setting.getString("SettingDownUp", mDownUp));
                        break;
                }
                return false;
            }
        });

        mButtonLeft = (Button) findViewById(R.id.LeftBtn);
        mButtonLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        send(sharedPreferences_setting.getString("SettingLeftDown", mLeftDown));
                        break;
                    case MotionEvent.ACTION_UP:
                        send(sharedPreferences_setting.getString("SettingLeftUp", mLeftUp));
                        break;
                }
                return false;
            }
        });

        mButtonRight = (Button) findViewById(R.id.RightBtn);
        mButtonRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        send(sharedPreferences_setting.getString("SettingRightDown", mRightDown));
                        break;
                    case MotionEvent.ACTION_UP:
                        send(sharedPreferences_setting.getString("SettingRightUp", mRightUp));
                        break;
                }
                return false;
            }
        });

        mButtonForward.setEnabled(false);
        mButtonBackward.setEnabled(false);
        mButtonTurnLeft.setEnabled(false);
        mButtonTurnRight.setEnabled(false);
        mButtonUp.setEnabled(false);
        mButtonDown.setEnabled(false);
        mButtonLeft.setEnabled(false);
        mButtonRight.setEnabled(false);
    }
}
