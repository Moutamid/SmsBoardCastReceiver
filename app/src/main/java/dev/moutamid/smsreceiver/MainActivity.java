package dev.moutamid.smsreceiver;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.widget.TextView;
import android.widget.Toast;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        if (!sp.getBoolean("first", false)) {
            new AlertDialog.Builder(this)
                    .setMessage("Please allow contact permissions for the app to work!\nYour latest received messages will be displayed in the middle of this screen!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Permissions.check(MainActivity.this, Manifest.permission.RECEIVE_SMS, null, new PermissionHandler() {
                                @Override
                                public void onGranted() {
                                    Intent backgroundService = new Intent(getApplicationContext(), SmsReceiverService.class);
                                    startService(backgroundService);
                                    Toast.makeText(MainActivity.this, "Cool!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .show();
            sp.edit().putBoolean("first", true).apply();
        } else {
            Intent backgroundService = new Intent(getApplicationContext(), SmsReceiverService.class);
            startService(backgroundService);
        }

        TextView textView = findViewById(R.id.messageTextView);

        String data = "This app has been created by moutamid_waseem on fiverr\n"
                + "NUMBER: " + sp.getString("msg_from", "null") + "\n"
                + "MESSAGE: " + sp.getString("msgBody", "null");

        textView.setText(data);

//        registerReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView textView = findViewById(R.id.messageTextView);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String data = "This app has been created by moutamid_waseem on fiverr\n"
                + "NUMBER: " + sp.getString("msg_from", "null") + "\n"
                + "MESSAGE: " + sp.getString("msgBody", "null");

        textView.setText(data);
    }

    // FOR SCREEN TURN ON BROADCAST RECEIVER
    private void registerReceiver() {

        final IntentFilter theFilter = new IntentFilter();

        theFilter.addAction(Intent.ACTION_SCREEN_ON);
        theFilter.addAction(Intent.ACTION_SCREEN_OFF);
        theFilter.addAction(Intent.ACTION_USER_PRESENT);

        BroadcastReceiver screenOnOFReceiver1 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String strAction = intent.getAction();

                KeyguardManager kgMgr = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

                if (strAction.equals(Intent.ACTION_SCREEN_OFF)) {

                }
                if (strAction.equals(Intent.ACTION_SCREEN_ON)) {

                }
                if (strAction.equals(Intent.ACTION_SCREEN_OFF)) {

                }
                if (strAction.equals(Intent.ACTION_USER_PRESENT) && !kgMgr.inKeyguardRestrictedInputMode()) {
                    // DEVICE UN-LOCKED

                    Toast.makeText(context, "UNLOCKED!", Toast.LENGTH_SHORT).show();
//                    int resID = getResources().getIdentifier("whistle", "raw", getPackageName());

//                    MediaPlayer mediaPlayer = MediaPlayer.create(context, resID);
//                    mediaPlayer.start();

                } else {
                    // DEVICE LOCKED
                }

            }
        };
        getApplicationContext().registerReceiver(screenOnOFReceiver1, theFilter);
    }

}