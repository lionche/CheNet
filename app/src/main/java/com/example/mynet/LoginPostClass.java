package com.example.mynet;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.mynet.javabean.LoginPostBean;
import com.example.mynet.javabean.LoginResponseBean;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.*;

import static com.example.mynet.MainActivity.loginCallBackListener;

public class LoginPostClass {
    static String TAG = "testhttp";
    public static LoginResponseBean loginResponseBean;

    public static void LoginPost(LoginPostBean loginPostBean) {
        String namePost = loginPostBean.getName();
        String passwordPost = loginPostBean.getPassword();
        String ipPost = loginPostBean.getIpadr();
        String macPost = loginPostBean.getMacadr();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(2, TimeUnit.SECONDS)
                .build();
        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType, "{\"deviceType\":\"Android\",\"webAuthUser\":\"" + namePost + "\",\"webAuthPassword\":\"" + passwordPost + "\",\"redirectUrl\":\"http://10.16.0.12:8081/?usermac=" + macPost + "&userip=" + ipPost + "&origurl=http://edge.microsoft.com/captiveportal/generate_204&nasip=10.100.0.1\",\"type\":\"login\"}");
        Log.d(TAG, "登录信息，{\"deviceType\":\"Android\",\"webAuthUser\":\"" + namePost + "\",\"webAuthPassword\":\"" + passwordPost + "\",\"redirectUrl\":\"http://10.16.0.12:8081/?usermac=" + macPost + "&userip=" + ipPost + "&origurl=http://edge.microsoft.com/captiveportal/generate_204&nasip=10.100.0.1\",\"type\":\"login\"}");
        Request request = new Request.Builder()
                .url("http://10.16.0.12:8081/portal/api/v2/online")
                .method("POST", body)
                .addHeader("accept", "*/*")
                .addHeader("accept-encoding", "gzip, deflate")
                .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6")
                .addHeader("authorization", "d258e916aa384de449a489e2abf0642e394dc69548cdbdda602fa64a6f3469de083c110756e9cefd9270b6d7cc354ff123223db4cf860564")
                .addHeader("content-length", "251")
                .addHeader("content-type", "application/json")
//                .addHeader("cookie", "redirectUrl=http://10.16.0.12:8081/?usermac=2C-61-04-FF-73-68&userip=10.22.193.12&origurl=http://edge.microsoft.com/captiveportal/generate_204&nasip=10.100.0.1; token=d258e916aa384de449a489e2abf0642e394dc69548cdbdda602fa64a6f3469de083c110756e9cefd9270b6d7cc354ff123223db4cf860564")
                .addHeader("dnt", "1")
                .addHeader("host", "10.16.0.12:8081")
                .addHeader("origin", "http://10.16.0.12:8081")
                .addHeader("proxy-connection", "keep-alive")
                .addHeader("referer", "http://10.16.0.12:8081/login")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36 Edg/86.0.622.69")
                .build();
//        Response response = client.newCall(request).execute();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "发送错误"+e.getMessage());
                loginCallBackListener.LoginSendMessage(false, 't');

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String responseData = response.body().string();
                //返回200为登录正确 400为错误
                char checkLogin = responseData.toString().charAt(14);
                char checkwrong = responseData.toString().charAt(90);
                Log.d(TAG, "接受信息"+responseData.toString());

                if (checkLogin == '2') {

                    loginCallBackListener.LoginSendMessage(true, 's');
                }
                else {
                    loginCallBackListener.LoginSendMessage(false, checkwrong);
                }

                loginResponseBean = JSON.parseObject(responseData, LoginResponseBean.class);
                RequestDevicesClass.RequestDevices();

            }


        });
    }


}
