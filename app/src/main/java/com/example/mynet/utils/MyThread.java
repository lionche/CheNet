package com.example.mynet.utils;


import android.util.Log;

public class MyThread extends Thread {
    private static final String TAG = "testhttp";
    public static boolean WebValidate;

    public void run() {
        WebValidate = GetAddress.isAvailableByPing("www.baidu.com");
        Log.d(TAG, "网络: " + GetAddress.isAvailableByPing("www.baidu.com"));


    }
}

