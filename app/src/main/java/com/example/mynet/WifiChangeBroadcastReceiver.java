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




        String wifiname = intent.getStringExtra(ConnectivityManager.EXTRA_EXTRA_INFO);

        int nettype =  intent.getIntExtra(ConnectivityManager.EXTRA_NETWORK_TYPE,100);

        Boolean ifconnect = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false);

        int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);



//        Log.d("testhttp", "onReceive: "+nettype +" "+ifconnect +" "+wifistate);



        if(nettype == 1 && !ifconnect && wifistate == 4){

            Log.d("testhttp", "onReceive: 我收到wifi切换广播：打开 ");
            wifiCallBackListener.WifiSendMessage(5);


//            wifiCallBackListener.WifiSendMessage(8);

            Log.d("testhttp", "onReceive: "+wifiname);
            if (wifiname.contains("STUDENT")){
                Log.d("testhttp", "onReceive: 你链接了student-net");
                isNWU_Student = true;
//                wifiCallBackListener.WifiSendMessage(6);
            }
        }else if(nettype == 100 && !ifconnect && (wifistate == 1)){
            Log.d("testhttp", "onReceive: 我收到wifi切换广播：关闭 ");
            wifiCallBackListener.WifiSendMessage(5);

        }

    }
}
