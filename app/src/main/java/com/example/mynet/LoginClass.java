package com.example.mynet;

import android.widget.EditText;

import static com.blankj.utilcode.util.DeviceUtils.getMacAddress;
import static com.blankj.utilcode.util.NetworkUtils.getIpAddressByWifi;

public class LoginClass {
    private EditText et_name;
    private EditText et_password;
    public static PostBean postBean;

    private static void getInfo(){
        postBean = new PostBean();
        postBean.setMacadr(getMacAddress());
        postBean.setIpadr(getIpAddressByWifi());
    }

    public static void getPostBean(){
        getInfo();

    }
    public static void login(){
        SendPost.LoginPost(postBean);
    }

}
