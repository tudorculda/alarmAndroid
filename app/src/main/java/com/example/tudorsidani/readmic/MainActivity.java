package com.example.tudorsidani.readmic;

import android.content.Context;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import utils.ArrayUtils;


public class MainActivity extends AppCompatActivity {
    private StatusChecker statusChecker;
    private TextView logView;
    PowerManager.WakeLock wl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logView = (TextView) findViewById(R.id.logOutText);
        statusChecker = new StatusChecker();
        statusChecker.setLog(this);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
        wl.acquire();


    }

    @Override
    protected void onStop() {

        super.onStop();

    }

    @Override
    protected void onDestroy(){
        statusChecker.isRunning = false;
        wl.release();
        super.onDestroy();

    }

    public void println(String msg)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
        logView.append("\n[" + df.format(new Date()) + "] " + msg);
    }

    public void startAlarm(View view)
    {

        SmsM smsDist = new SmsM();
        EditText ed = (EditText) findViewById( R.id.editText );
        smsDist.addPhone( ed.getText().toString() );
        ed = (EditText) findViewById( R.id.editText2 );
        smsDist.addPhone( ed.getText().toString() );
        ed = (EditText) findViewById( R.id.editText3 );
        smsDist.addPhone( ed.getText().toString() );
        statusChecker.setSmsDist(smsDist);
        ed = (EditText) findViewById( R.id.micThresh );
        MicGetterChecker mc = new MicGetterChecker( Double.parseDouble( ed.getText().toString() ) );
        mc.setLog(this);
        statusChecker.addChecker(mc);
        UsbChargerChecker u = new UsbChargerChecker(view.getContext());
        u.setLog(this);
        statusChecker.addInfoChecker(u);
        statusChecker.addInfoChecker(new InfoStatusChecker(u));
        Button bt = (Button) findViewById(R.id.rec_button );
        bt.setEnabled(false);
        Thread t = new Thread(statusChecker);
        t.start();
    }

}
