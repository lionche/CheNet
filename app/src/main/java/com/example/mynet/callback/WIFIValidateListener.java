package com.example.mynet.callback;

import com.google.android.material.snackbar.Snackbar;

import static com.example.mynet.MainActivity.coordinator;

public class WIFIValidateListener {
    private Listener mListener;
    private interface Listener{

        //SnackBar给提示
        void ShowSnackBar();

    }

    public void setmListener(WIFIValidateListener.Listener mListener) {
        this.mListener = mListener;
    }

    //WIFI关闭
    public void WifiDisabled(){
        if (mListener != null){
            mListener.ShowSnackBar();
        }
    }

    //WIFI打开，但没连WIFI
    public void WIFIEnableNoConection(){
        if (mListener != null){
            mListener.ShowSnackBar();
            Snackbar.make(coordinator, "登录成功啦 😚", Snackbar.LENGTH_LONG).show();
        }
    }

    //WIFI打开，连接WIFI，但已经登录
    public void WIFIEnableHaveConectionHaveLogin(){
        if (mListener != null){
            mListener.ShowSnackBar();
        }
    }

    //WIFI打开连接WIFI但没登录
        public void ReadyToLogin(){
        if (mListener != null){
            mListener.ShowSnackBar();
        }
    }


}
