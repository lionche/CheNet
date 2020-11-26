package com.example.mynet;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.example.mynet.callback.WIFICallBackListener;
import com.google.android.material.snackbar.Snackbar;

import static com.blankj.utilcode.util.NetworkUtils.getWifiEnabled;
import static com.blankj.utilcode.util.NetworkUtils.isAvailableByPing;
import static com.blankj.utilcode.util.NetworkUtils.isWifiConnected;
import static com.example.mynet.LoginClass.getPostBean;
import static com.example.mynet.LoginClass.login;
import static com.example.mynet.MainActivity.coordinator;
import static com.example.mynet.MainActivity.wifiCallBackListener;


public class WIFIValidate {

    private static final String TAG = "testhttp";



    public static void checkWIFIValidate() {
        Log.d(TAG, "checkWIFIValidate: 我在检测网络状况，检测完成我才发送");
        //Wi-Fi都没打开  1
        if (!getWifiEnabled()) {
            wifiCallBackListener.WifiSendMessage(1);
        } else {
            //WIFI打开但没连Wi-Fi  2
            if (!isWifiConnected()) {
                wifiCallBackListener.WifiSendMessage(2);
            } else {
                //Wi-Fi打开，连Wi-Fi，但是有网络 3
                if (isAvailableByPing("www.baidu.com")) {
                    Log.d(TAG, "checkWIFIValidate: 我ping通百度");
                    wifiCallBackListener.WifiSendMessage(3);
                } else {
                    //Wi-Fi打开，连Wi-Fi，但是无网络 4
                    Log.d(TAG, "checkWIFIValidate: 我ping不通百度");
                    wifiCallBackListener.WifiSendMessage(4);
                    wifiCallBackListener.ReadyToLogin();
                }
            }
        }

    }

}
