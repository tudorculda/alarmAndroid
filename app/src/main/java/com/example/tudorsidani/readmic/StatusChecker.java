package com.example.tudorsidani.readmic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tudor on 13.04.2017.
 */

public class StatusChecker implements Runnable{
    public volatile boolean isRunning;
    private SmsM smsDist;
    private MainActivity log;
    public List<Checker> checkerList = new ArrayList<>();
    private long checkTimeFreq_ms = 1000;
    private long reCheckAlarm_ms = 30 * 60 * 1000; //  30 minute
//private long reCheckAlarm_ms = 5 * 1000; //  30 minute
    private long lastTriggeredAlarmTime = 0;

    public void addChecker(Checker c){
        checkerList.add(c);
    }

    public void setSmsDist( SmsM s )
    {
        smsDist = s;
    }

    public void setLog( MainActivity l )
    {
        log = l;
    }

    private void printMsg( final String msg )
    {
        if (log!= null)
            log.runOnUiThread( new Runnable() {
                public void run() {
                    log.println( "StatusChecker:" + msg );
                }
            });
    }


    @Override
    public void run() {
        isRunning = true;
        long runningTime;
        boolean isAlarmTriggered;
        while (isRunning) {
            isAlarmTriggered = false;

            try {
                Thread.sleep(checkTimeFreq_ms);
                runningTime = System.currentTimeMillis();
                for( Checker c : checkerList){
                    if( ( runningTime - lastTriggeredAlarmTime > reCheckAlarm_ms ) &&!c.isStatusOk() && smsDist != null ){
                        isAlarmTriggered = true;

                        smsDist.sendSms(c.getErrorMessage());
//                        printMsg(c.getErrorMessage());
                    }
                }
                if(isAlarmTriggered)
                    lastTriggeredAlarmTime = runningTime;
            } catch (Exception ex) {
                printMsg(ex.getMessage());
            }

        }
        for( Checker c : checkerList){
            c.clearObject();
        }
        smsDist.sendSms("Aplicatia de alarma a fost inchisa");
    }
}
