package com.example.tudorsidani.readmic;

import android.os.BatteryManager;

import java.util.Calendar;

/**
 * Created by tudor on 14.04.2017.
 */

public class InfoStatusChecker implements Checker {

    private long lastStatus_ms = 0;
    private long minDeltaTime_ms = 5 * 60 * 60 * 1000;
    private final UsbChargerChecker usbChargerChecker;

    public InfoStatusChecker(UsbChargerChecker u){
        usbChargerChecker = u;
    }
    private int reportHour = 8;
    @Override
    public boolean isStatusOk() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        long t = System.currentTimeMillis();

        if (hour == reportHour && (t - lastStatus_ms) > minDeltaTime_ms ){
            lastStatus_ms = t;
            return false;
        }
        return true;
    }

    @Override
    public String getErrorMessage() {

        return " Status => Aplicatia functioneaza, nivelul bateriei este: " + usbChargerChecker.getBatteryLevel() + "%";
    }

    @Override
    public void clearObject() {

    }
}
