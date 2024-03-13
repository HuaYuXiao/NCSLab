package com.example.administrator.alphabot;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketActivity extends Activity {
    private TextView StateTextView = null;
    private Button connect = null;
    private Button buzzer = null;
    public Socket socket;
    private InputStream is = null;
    private OutputStream os = null;
    public boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.wifi_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        /* motor control button */
        Button forward = (Button) findViewById(R.id.ForwardBtn);
        Button backward = (Button) findViewById(R.id.BackwardBtn);
        Button turnleft = (Button) findViewById(R.id.TurnRightBtn);
        Button turnright = (Button) findViewById(R.id.TurnLeftBtn);

        /* servo control button */
        Button up = (Button) findViewById(R.id.UpBtn);
        Button down = (Button) findViewById(R.id.DownBtn);
        Button right = (Button) findViewById(R.id.RightBtn);
        Button left = (Button) findViewById(R.id.LeftBtn);

        StateTextView = (TextView) findViewById(R.id.TextView);  //show button state

        // 获得传进来的Intent
        try {

            socket = ((MySocket)getApplication()).getSocket();  // Scoket连接
            // 连接成功
            if (socket.isConnected() == true) {
                // 获得输出，输入流
                os = socket.getOutputStream();
                is = socket.getInputStream();
                connected = true; // 设置连接标记
                Toast.makeText(SocketActivity.this, R.string.SocketSucceed_hint, Toast.LENGTH_LONG).show();

                // 创建一个线程,用于接收服务器端数据
               // ReciveThread rThread = new ReciveThread();
                //new Thread(rThread, "ReciveThread").start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(SocketActivity.this, R.string.connectfail_hint,
                    Toast.LENGTH_LONG).show();
        }

        forward.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:    // button press
                        SendCommand("Forward");
                        break;
                    case MotionEvent.ACTION_UP:      //button release
                        SendCommand("Stop");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        backward.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        SendCommand("Backward");
                        break;
                    case MotionEvent.ACTION_UP:
                        SendCommand("Stop");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        turnleft.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        SendCommand("TurnLeft");
                        break;
                    case MotionEvent.ACTION_UP:
                        SendCommand("Stop");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        turnright.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        SendCommand("TurnRight");
                        break;
                    case MotionEvent.ACTION_UP:
                        SendCommand("Stop");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        up.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        SendCommand("Up");
                        break;
                    case MotionEvent.ACTION_UP:
                        SendCommand("Stop");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        down.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        SendCommand("Down");
                        break;
                    case MotionEvent.ACTION_UP:
                        SendCommand("Stop");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        right.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        SendCommand("Right");
                        break;
                    case MotionEvent.ACTION_UP:
                        SendCommand("Stop");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        left.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        SendCommand("Left");
                        break;
                    case MotionEvent.ACTION_UP:
                        SendCommand("Stop");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });


    }

    // 发送字符串到Socket
    protected void SendCommand(String command) {
        //Log.d("MainActivity", "SendCommand: " + Thread.currentThread().getName());
        try {
            os.write(command.getBytes());    // 写Socket
            os.flush();                      // 刷新缓冲区
            StateTextView.setText(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
