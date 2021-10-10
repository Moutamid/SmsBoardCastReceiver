package dev.moutamid.smsreceiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsListener extends BroadcastReceiver {
    private static final String TAG = "SmsListener";

//    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);


        // TODO Auto-generated method stub
        Log.d(TAG, "onReceive: started");
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
//            Toast.makeText(context, "if (intent.getAction().equals(\"android.provider.Telephony.SMS_RECEIVED\")) {", Toast.LENGTH_SHORT).show();
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null) {
                //---retrieve the SMS message received---
//                Toast.makeText(context, "Bundle is not null", Toast.LENGTH_SHORT).show();
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        Log.d(TAG, "onReceive: msg_from: " + msg_from);
                        Log.d(TAG, "onReceive: msgBody: " + msgBody);
                        sp.edit().putString("msg_from", msg_from).apply();
                        sp.edit().putString("msgBody", msgBody).apply();
                        NotificationHelper notificationHelper = new NotificationHelper(context);
                        notificationHelper.sendHighPriorityNotification(
                                "You a got a notification!",
                                "Received a new message!",
                                MainActivity.class
                        );
//                        Toast.makeText(context, msg_from, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(context, msgBody, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                            Log.d("Exception caught",e.getMessage());
                }
            } else {
                Log.d(TAG, "onReceive: bundle is null");
                Toast.makeText(context, "Bundle is null", Toast.LENGTH_SHORT).show();
            }
        }

//        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
//            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
//                String messageBody = smsMessage.getMessageBody();
//                Log.d(TAG, "onReceiveOther: messageBody: " + messageBody);
//                Toast.makeText(context, messageBody, Toast.LENGTH_SHORT).show();
//            }
    }

}

