package dev.moutamid.smsreceiver;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.provider.Telephony;
import android.util.Log;

import androidx.annotation.Nullable;

public class SmsReceiverService extends Service {

    private static final String TAG = "SmsReceiverListener";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private SmsListener smsListener;

    @Override
    public void onCreate() {
        super.onCreate();
        smsListener = new SmsListener();
        IntentFilter filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
//        IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(smsListener, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Unregister screenOnOffReceiver when destroy.
        if (smsListener != null) {
            unregisterReceiver(smsListener);
            Log.d(TAG, "Service onDestroy: SmsListenerReceiverAndService is unregistered.");
        }
    }
}
