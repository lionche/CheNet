package com.example.mynet.callback;

public class LoginCallBackListener {
    private Listener mListener;

    public interface Listener{
        void sendMessage();
    }

    public void setmListener(Listener mListener) {
        this.mListener = mListener;
    }

    public void getMessage(){
        if (mListener != null){
            mListener.sendMessage();
        }
    }


}
