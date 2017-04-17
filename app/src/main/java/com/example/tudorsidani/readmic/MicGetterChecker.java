package com.example.tudorsidani.readmic;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import utils.ArrayUtils;

/**
 * Created by tudorsidani on 29.06.2016.
 */
public class MicGetterChecker implements Checker{

    private AudioRecord rec;
    private int sampleRate = 8000;
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int minbuffer;
    private MainActivity log;
    private String lastError = "no errors in mic analysis";


    private final  long  stp_trigger = 150000;

    private int readingTime_millis = 50;





    public MicGetterChecker( )
    {
        minbuffer =  AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
        rec = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, minbuffer);
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

       if (rec!=null)
           rec.release();
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
        long stp = 0;
        for (int i = 0; i < samples.length; i++) {
            stp +=  samples[i]*samples[i];
        }

        stp /= samples.length;
//        printMsg("stp=" + stp);
        if ( stp > stp_trigger ) {
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
