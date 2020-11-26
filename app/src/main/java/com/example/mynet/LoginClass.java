package com.example.mynet;

import android.widget.EditText;

import static com.blankj.utilcode.util.DeviceUtils.getMacAddress;
import static com.blankj.utilcode.util.NetworkUtils.getIpAddressByWifi;

public class LoginClass {
    private EditText et_name;
    private EditText et_password;
    public static PostBean postBean;


    public static void getPostBean(){
        postBean = new PostBean();
        postBean.setMacadr(getMacAddress());
        postBean.setIpadr(getIpAddressByWifi());


    }
    public static void login(){
        SendPost.LoginPost(postBean);
    }

}
