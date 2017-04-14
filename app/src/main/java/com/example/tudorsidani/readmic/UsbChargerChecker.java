package com.example.tudorsidani.readmic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * Created by tudor on 14.04.2017.
 */

public class UsbChargerChecker  extends BroadcastReceiver implements Checker{
    private String lastError = "Telefonul nu incarca, problema poate fi bateria generala sau mufa usb deconectata, nivelul bateriei telefonului este ";
    private Intent intent;
    private int level;
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
    @Override
    public boolean isStatusOk() {

        return isPlugged;

    }

    @Override
    public String getErrorMessage() {
        return lastError + getBatteryLevel() + "%";
    }

    public int getBatteryLevel(){
        return  intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
    }

    @Override
    public void clearObject() {

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
            intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
    }
}
