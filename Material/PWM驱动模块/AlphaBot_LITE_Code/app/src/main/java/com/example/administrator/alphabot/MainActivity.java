package com.example.administrator.alphabot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void bluetoothBtn(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, DeviceScanActivity.class);
        startActivity(intent);
        finish();
    }

    public void wifiBtn(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
