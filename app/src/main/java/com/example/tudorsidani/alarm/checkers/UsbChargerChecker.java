package com.example.tudorsidani.alarm.checkers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.example.tudorsidani.alarm.MainActivity;
import com.example.tudorsidani.alarm.checkers.InfoChecker;

import java.util.Calendar;

/**
 * Created by tudor on 14.04.2017.
 */

public class UsbChargerChecker  extends BroadcastReceiver implements InfoChecker {

    private long lastStatus_ms = 0;
    private long minDeltaTime_ms = 2 * 60 * 60 * 1000;


    private String lastError = "Telefonul nu incarca, nivelul bateriei=";
    private Intent intent;
    private int level = 0;
    boolean isPlugged = true;
    private MainActivity log;
    public void setLog( MainActivity l )
    {
        log = l;
    }

    public UsbChargerChecker() {
    }

    private void printMsg(final String msg )
    {
        if (log!= null)
            log.runOnUiThread( new Runnable() {
                public void run() {
                    log.println("UsbCharger:" +  msg );
                }
            });
    }
    public UsbChargerChecker(Context context){
        intent = context.registerReceiver(this, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        context.registerReceiver(this,new IntentFilter(Intent.ACTION_POWER_DISCONNECTED));
        context.registerReceiver(this,new IntentFilter(Intent.ACTION_POWER_CONNECTED));
    }


    public int getBatteryLevel(){
        return  level;
    }


    public void onReceive(Context context , Intent intent) {
        String action = intent.getAction();

        if(action.equals(Intent.ACTION_POWER_CONNECTED)) {
            isPlugged = true;
        }
        else if(action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
            isPlugged = false;
        }
        if(action.equals(Intent.ACTION_BATTERY_CHANGED))
            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
    }

    @Override
    public boolean isTimeToReport() {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        long t = System.currentTimeMillis();

        if ( ((t - lastStatus_ms) > minDeltaTime_ms) && !isPlugged ){
            lastStatus_ms = t;
            return true;
        }
        return false;
    }

    @Override
    public String getMessage() {
        return lastError + getBatteryLevel() + "%";
    }
}
