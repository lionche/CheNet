package com.example.mynet.callback;

import com.google.android.material.snackbar.Snackbar;

public class WIFICallBackListener {
    private Listener mListener;
    public interface Listener{

        //SnackBar给提示
        void ShowTips(Snackbar snackbar);
        void toLogin();

    }

    public void setmListener(WIFICallBackListener.Listener mListener) {
        this.mListener = mListener;
    }

    //WIFI关闭
    //WIFI打开，但没连WIFI
    //WIFI打开，连接WIFI，但已经登录
    public void WifiShowTips(Snackbar snackbar){
        if (mListener != null){
            mListener.ShowTips(snackbar);
        }
    }

    //WIFI打开连接WIFI但没登录
        public void ReadyToLogin(){
        if (mListener != null){
            mListener.toLogin();
        }
    }


}
