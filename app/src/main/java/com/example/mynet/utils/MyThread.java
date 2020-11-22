package com.example.mynet.utils;


import android.util.Log;

import static com.blankj.utilcode.util.NetworkUtils.isAvailableByPing;

public class MyThread extends Thread {
    private static final String TAG = "testhttp";
    public static boolean WebValidate;

    public void run() {
        WebValidate = isAvailableByPing("www.baidu.com");
        Log.d(TAG, "网络: " + WebValidate);


    }
}

