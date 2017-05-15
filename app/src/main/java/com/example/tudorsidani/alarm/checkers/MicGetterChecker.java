package com.example.tudorsidani.alarm.checkers;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.example.tudorsidani.alarm.MainActivity;
import com.example.tudorsidani.alarm.checkers.Checker;

/**
 * Created by tudorsidani on 29.06.2016.
 */
public class MicGetterChecker implements Checker {

    private AudioRecord rec;
    private int sampleRate = 8000;
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int minbuffer;
    private MainActivity log;
    private String lastError = "no errors in mic analysis";


    private double stp_trigger = 380;

    private int readingTime_millis = 50;

    private final int consecutiveCountTrhesh = 3;

    private int count = 0;




    public MicGetterChecker( )
    {
        minbuffer =  AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
        rec = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, minbuffer);
    }

    public MicGetterChecker( double micTresh )
    {
        this();
        stp_trigger = micTresh;
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
                    log.println("MicrophoneGetter:" +  msg );
                }
            });
    }

    public short[] getSamples(int millisec)
    {

        short[] rawShorts = new short[ sampleRate * millisec / 1000 ];
        rec.startRecording();
        rec.read(rawShorts, 0, rawShorts.length);
        rec.stop();
        return rawShorts;
    }

    protected void finalize()
    {

       if (rec!=null) {
           rec.release();
           rec = null;
       }
        printMsg("Mic object released");
        try {
            super.finalize();
        } catch (Throwable throwable) {
            printMsg("Error on mic finalize");
            throwable.printStackTrace();
        }
    }


    @Override
    public boolean isStatusOk() {
        short[] samples = getSamples( readingTime_millis );
        double stp = 0;
        for (int i = 0; i < samples.length; i++) {
            stp +=  samples[i]*samples[i];
        }

        stp = Math.sqrt(stp) / samples.length;
        if ( stp > stp_trigger ) {
            count++;
            printMsg("MicLevel=" + (int)stp+ " , count=" + count);
        }
        else{
            count = 0;
        }

        if ( count >= consecutiveCountTrhesh ) {
            count = 0;

            lastError = "Atentie! Alarma a fost declansata!";
            return false;
        }
        return true;
    }

    @Override
    public String getErrorMessage() {
        return lastError;
    }

    @Override
    public void clearObject() {
        finalize();

    }
}
