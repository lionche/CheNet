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


public class WIFIValidate {

    private static final String TAG = "testhttp";

    static WIFICallBackListener wifiCallBackListener = new WIFICallBackListener();

    public static void WIFICallBack() {
        Log.d(TAG, "WIFICallBack: 我在等网络状况的消息");


        wifiCallBackListener.setmListener(new WIFICallBackListener.Listener() {

            @Override
            public void SendWIFIMessage(int caseid) {
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putInt("WIFICallBack", caseid);
                message.setData(bundle);
                MainActivity.handler.sendMessage(message);
            }

            @Override
            public void toLogin() {
                getPostBean();
            }
        });
    }

    public static void checkWIFIValidate() {
        Log.d(TAG, "checkWIFIValidate: 我在检测网络状况");
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

                    wifiCallBackListener.WifiSendMessage(3);
                } else {
                    //Wi-Fi打开，连Wi-Fi，但是无网络 4
                    wifiCallBackListener.WifiSendMessage(4);
//                    wifiCallBackListener.ReadyToLogin();
                }
            }
        }

    }

}
