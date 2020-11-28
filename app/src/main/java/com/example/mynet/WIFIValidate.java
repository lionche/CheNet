package com.example.mynet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.example.mynet.callback.WIFICallBackListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.Timer;
import java.util.TimerTask;

import static com.blankj.utilcode.util.NetworkUtils.getWifiEnabled;
import static com.blankj.utilcode.util.NetworkUtils.isAvailableByPing;
import static com.blankj.utilcode.util.NetworkUtils.isAvailableByPingAsync;
import static com.blankj.utilcode.util.NetworkUtils.isWifiConnected;
import static com.example.mynet.LoginClass.getPostBean;
import static com.example.mynet.LoginClass.login;
import static com.example.mynet.MainActivity.coordinator;
import static com.example.mynet.MainActivity.wifiCallBackListener;


public class WIFIValidate {

    private static final String TAG = "testhttp";
     static boolean isAvailableByPing = false;




    public static void checkWIFIValidate() {

        Log.d(TAG, "checkWIFIValidate: 我在检测网络状况，检测完成我才发送");



        //Wi-Fi都没打开  1
        if (!getWifiEnabled()) {
            Log.d(TAG, "checkWIFIValidate: 检测wifi开没开");
            wifiCallBackListener.WifiSendMessage(1);
        } else {
            //WIFI打开但没连Wi-Fi  2
            if (!isWifiConnected()) {
                Log.d(TAG, "checkWIFIValidate: 检测wifi练没练");
                wifiCallBackListener.WifiSendMessage(2);
            }else{
                NewThreadToPing();

                NewThreadSendMessage();
            }

        }

    }

    private static void NewThreadToPing() {
        isAvailableByPing = false;
        new Thread() {
            @Override
            public void run() {
                isAvailableByPing = isAvailableByPing("www.baidu.com");
            }
        }.start();
    }

    private static void NewThreadSendMessage() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    Log.d(TAG, "checkWIFIValidate我等了1S: " + isAvailableByPing);
                    if (isAvailableByPing){
                        Log.d(TAG, "checkWIFIValidate: 我ping通百度");
                        wifiCallBackListener.WifiSendMessage(3);
                    }else {
                        Log.d(TAG, "checkWIFIValidate: 我ping不通百度");
                        wifiCallBackListener.WifiSendMessage(4);
                        wifiCallBackListener.ReadyToLogin();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }


}
