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

import java.util.Timer;
import java.util.TimerTask;

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

        String data = "NUMBER: " + sp.getString("msg_from", "null") + "\n"
                + "MESSAGE: " + sp.getString("msgBody", "null");

        /*String data = "This app has been created by moutamid_waseem on fiverr\n"
                + "NUMBER: " + sp.getString("msg_from", "null") + "\n"
                + "MESSAGE: " + sp.getString("msgBody", "null");*/

        textView.setText(data);

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView1 = findViewById(R.id.messageTextView);

                        try {
                            SharedPreferences sp1 = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        /*                    String data = "This app has been created by moutamid_waseem on fiverr\n"
                                    + "NUMBER: " + sp1.getString("msg_from", "null") + "\n"
                                    + "MESSAGE: " + sp1.getString("msgBody", "null");
*/
                            String data = "NUMBER: " + sp.getString("msg_from", "null") + "\n"
                                    + "MESSAGE: " + sp.getString("msgBody", "null");

                            textView1.setText(data);

                        } catch (Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }
                });

            }
        };
        timer.schedule(timerTask, 1000, 200);
//        registerReceiver();
    }

    Timer timer;
    TimerTask timerTask;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView textView = findViewById(R.id.messageTextView);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
/*
        String data = "This app has been created by moutamid_waseem on fiverr\n"
                + "NUMBER: " + sp.getString("msg_from", "null") + "\n"
                + "MESSAGE: " + sp.getString("msgBody", "null");
*/
        String data = "NUMBER: " + sp.getString("msg_from", "null") + "\n"
                + "MESSAGE: " + sp.getString("msgBody", "null");

        textView.setText(data);
    }

}