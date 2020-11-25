package com.example.mynet.callback;

public class LoginCallBackListener {
    private Listener mListener;

    public interface Listener{
        void loginSuccess();
        void loginFail();

    }

    public void setmListener(Listener mListener) {
        this.mListener = mListener;
    }

    public void sentSuccessMessage(){
        if (mListener != null){
            mListener.loginSuccess();
        }
    }

    public void sentFailMessage(){
        if (mListener != null){
            mListener.loginFail();
        }
    }

}
