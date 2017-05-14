package com.example.tudorsidani.alarm.checkers;

import java.util.Calendar;

/**
 * Created by tudor on 14.04.2017.
 */

public class InfoStatusChecker implements InfoChecker {

    private long lastStatus_ms = 0;
    private long minDeltaTime_ms = 5 * 60 * 60 * 1000;
    private final UsbChargerChecker usbChargerChecker;

    public InfoStatusChecker(UsbChargerChecker u){
        usbChargerChecker = u;
    }
    private int reportHour = 8;




    @Override
    public boolean isTimeToReport() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        long t = System.currentTimeMillis();

        if (hour == reportHour && (t - lastStatus_ms) > minDeltaTime_ms ){
            lastStatus_ms = t;
            return true;
        }
        return false;
    }

    @Override
    public String getMessage() {
        return "Aplicatia functioneaza, nivelul bateriei este: " + usbChargerChecker.getBatteryLevel() + "%";
    }
}
