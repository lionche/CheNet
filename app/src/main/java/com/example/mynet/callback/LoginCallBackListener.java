package com.example.mynet.callback;

public class LoginCallBackListener {
    private Listener mListener;

    public interface Listener{

        void SendLoginMessage(Boolean b);

    }

    public void setmListener(Listener mListener) {
        this.mListener = mListener;
    }

    public void LoginSendMessage(Boolean b){
        if (mListener != null){
            mListener.SendLoginMessage(b);
        }
    }


}
