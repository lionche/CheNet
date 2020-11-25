package com.example.mynet;

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
            public void ShowTips(Snackbar snackbar) {
                snackbar.show();
            }

            @Override
            public void toLogin() {
                getPostBean();
            }
        });
    }

    public static void checkWIFIValidate() {
        Log.d(TAG, "checkWIFIValidate: 我在检测网络状况");
        Snackbar snackbar = null;
        //Wi-Fi都没打开
        if (!getWifiEnabled()) {
            Log.d(TAG, "checkWIFIValidate: WIFI都没打开哥");
            snackbar = Snackbar.make(coordinator, "WIFI都没打开哥 😭", Snackbar.LENGTH_LONG);
        }else {
            //WIFI打开但没连Wi-Fi
            if (!isWifiConnected()){
                Log.d(TAG, "checkWIFIValidate: 这就来找我了 \n你咋不瞅瞅你连WIFI了没");
                snackbar = Snackbar.make(coordinator, "这就来找我了 \n你咋不瞅瞅你连WIFI了没👀", Snackbar.LENGTH_LONG);
            }else {
                //Wi-Fi打开，连Wi-Fi，但是有网络
                if (isAvailableByPing("www.baidu.com")){
                    Log.d(TAG, "checkWIFIValidate: 哈哈哈哈哈哈哈哈你其实已经登陆咯");
                    snackbar = Snackbar.make(coordinator, "哈哈哈哈哈哈哈哈,\n你其实已经登陆咯😙", Snackbar.LENGTH_LONG);
                }
                else {
                    Log.d(TAG, "checkWIFIValidate: 让我帮你登录叭");
                    snackbar = Snackbar.make(coordinator, "让我帮你登录叭😃", Snackbar.LENGTH_LONG);
                    wifiCallBackListener.ReadyToLogin();
                }
            }
        }
        wifiCallBackListener.WifiShowTips(snackbar);

    }
}
