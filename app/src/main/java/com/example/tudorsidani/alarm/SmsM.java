package com.example.tudorsidani.alarm;

import android.telephony.SmsManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tudorsidani on 08.07.2016.
 */
public class SmsM {
    private ArrayList<String> phoneNumbers = new ArrayList<>();

    public void addPhone( String phone)
    {
        phoneNumbers.add( phone );
    }


    public void sendSms( String msg )
    {
        SimpleDateFormat df = new SimpleDateFormat("'Data:' yyyy/MMM/d ' Ora:' hh:mm:ss");
        String msgWithDetails = "Mesaj de la alarma la\n" + df.format(new Date()) + "\n cu urmatorul text:\n" + msg;
        SmsManager smsManager = SmsManager.getDefault();

        if (smsManager == null ){
            throw new IllegalArgumentException("Sms manager nu poate fi initializat");
        }
        for( String phone : phoneNumbers ) {
            if (phone.isEmpty() )
                try{
                    smsManager.sendTextMessage( phone, null, msgWithDetails, null, null );
                } catch (Exception ex){
                    throw new IllegalArgumentException("Sms nu poate fi trimis, verifica permisiunile aplicatiei sau creditul pe cartela. Sms=" + msgWithDetails);
                }
        }
    }
}
