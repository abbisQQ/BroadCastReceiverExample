package com.abbisqq.broadcastreceiverexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView myImage;
    IntentFilter intentFilter;
    MyBroadCastReceiver receiver;
    boolean isCharging= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myImage = (ImageView)findViewById(R.id.myview);

        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        receiver = new MyBroadCastReceiver();

    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();


        /** Determine the current charging state **/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);

            isCharging(batteryManager.isCharging());
        } else {

            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

            Intent currentBatteryStatusIntent = registerReceiver(null, ifilter);

            int batteryStatus = currentBatteryStatusIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = batteryStatus == BatteryManager.BATTERY_STATUS_CHARGING ||
                    batteryStatus == BatteryManager.BATTERY_STATUS_FULL;

            isCharging(isCharging);
        }
        

        registerReceiver(receiver,intentFilter);
    }

    private void isCharging(boolean isCharging){
        if(isCharging)
            myImage.setImageResource(R.drawable.charging);
        else{
            myImage.setImageResource(R.drawable.nocharging);
        }
    }





    private class MyBroadCastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            boolean isCharging = action.equals(Intent.ACTION_POWER_CONNECTED);
            isCharging(isCharging);
        }
    }








}
