package com.example.tudorsidani.alarm.checkers;

import java.util.Calendar;

/**
 * Created by tudor on 15.05.2017.
 */

public class LowBatteryChecker implements InfoChecker {

    private long lastStatus_ms = 0;
    private long minDeltaTime_ms = 3 * 60 * 60 * 1000;
    int batteryTresh = 20;
    private final UsbChargerChecker usbChargerChecker;

    public LowBatteryChecker(UsbChargerChecker u){
        usbChargerChecker = u;
    }
    @Override
    public boolean isTimeToReport() {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        long t = System.currentTimeMillis();

        if ( ((t - lastStatus_ms) > minDeltaTime_ms) && usbChargerChecker.getBatteryLevel() < batteryTresh ){
            lastStatus_ms = t;
            return true;
        }
        return false;
    }

    @Override
    public String getMessage() {
        return "Avertizare, baterie descarcata, nivelul actual este: " + usbChargerChecker.getBatteryLevel() + "%";
    }
}
