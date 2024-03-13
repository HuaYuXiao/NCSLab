package com.example.administrator.alphabot;
import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Administrator on 2017/3/23.
 */

public class LoginActivity extends Activity {
    private EditText IPAddredit = null;
    private EditText CtrlPortedit = null;
    private EditText PortEdt = null;
    private WifiManager wifi = null;
    private InputStream is = null;            // http Input Stream
    private Socket socket;
    public boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        // 必须设置以下，否则无法Socket连接
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
                .build());

        //Button button1 = (Button) findViewById(R.id.button_go);
        IPAddredit = (EditText) findViewById(R.id.editIP); // 视频地址
        CtrlPortedit = (EditText) findViewById(R.id.editCtrlPort);// Socket端口
    }

    // 绑定连接按键
    public void connectBtn(View v) {
        String ip = IPAddredit.getText().toString();
        String Socketport = CtrlPortedit.getText().toString(); // Socket

        // 为Socket按键绑定监听器
        int port = Integer.parseInt(Socketport);// 端口必须是整型
        try {
            socket = new Socket();// 创建Scoket连接
            socket.connect(new InetSocketAddress(ip, port),1000);// Socket绑定ip和端口
            // 连接成功
            if (socket.isConnected() == true) {

                Toast.makeText(this, R.string.SocketSucceed_hint, Toast.LENGTH_LONG).show();
                ((MySocket)getApplication()).setSocket(socket);
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, SocketActivity.class);
                startActivity(intent);

            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.connectfail_hint, Toast.LENGTH_LONG).show();
        }
    }
}