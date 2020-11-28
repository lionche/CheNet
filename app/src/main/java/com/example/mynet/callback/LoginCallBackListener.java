package com.example.mynet.callback;

public class LoginCallBackListener {
    private Listener mListener;

    public interface Listener{

        void SendLoginMessage(Boolean b,char c);

    }

    public void setmListener(Listener mListener) {
        this.mListener = mListener;
    }

    public void LoginSendMessage(Boolean b,char c){
        if (mListener != null){
            mListener.SendLoginMessage(b,c);
        }
    }


}
