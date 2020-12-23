package com.example.mynet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.Utils;

import static com.blankj.utilcode.util.ActivityUtils.getTopActivity;
import static com.example.mynet.MainActivity.wifiCallBackListener;
import static com.example.mynet.WIFIValidate.checkWIFIValidate;

public class WifiChangeBroadcastReceiver extends BroadcastReceiver {

    public static boolean isNWU_Student = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("Action: " + intent.getAction() + "\n");
        sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
        String log = sb.toString();
        Log.d("testhttp", "onReceive: 我收到wifi切换广播 "+log);
        Log.d("testhttp", "onReceive: "+intent.getStringExtra(ConnectivityManager.EXTRA_EXTRA_INFO));

        String wifiname = intent.getStringExtra(ConnectivityManager.EXTRA_EXTRA_INFO);

        wifiCallBackListener.WifiSendMessage(8);

        if(wifiname != null){
            Log.d("testhttp", "onReceive: "+wifiname);
//            if (wifiname.contains("\"NWU-STUDENT\"")){
            if (wifiname.contains("XKIMG")){
                Log.d("testhttp", "onReceive: 你链接了student-net");
                isNWU_Student = true;
//                wifiCallBackListener.WifiSendMessage(6);
            }
        }

        int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
        switch (wifistate){
            case 1:
                Log.d("testhttp", "onReceive: :关闭 "+wifistate);
                wifiCallBackListener.WifiSendMessage(5);
                break;
            case 3:

        }
    }
}
