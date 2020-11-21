package com.example.mynet;

import android.util.Log;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

import okhttp3.*;

import static com.example.mynet.MainActivity.coordinator;

public class SendPost {
    static String TAG = "testhttp";
    static String namePost = null;
    static String passwordPost = null;
    static String ipPost = null;
    static String macPost = null;


    public static void getInfo(String name,String password,String ip ,String mac){
        namePost = name;
        passwordPost = password;
        ipPost =  ip;
        macPost = mac;
    }



    public static void LoginPost() {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, "{\"deviceType\":\"PC\",\"webAuthUser\":\"202032908\",\"webAuthPassword\":\"09005X\",\"redirectUrl\":\"http://10.16.0.12:8081/?usermac=4C-6F-9C-02-E2-C5&userip=10.21.91.241&origurl=http://edge.microsoft.com/captiveportal/generate_204&nasip=10.100.0.1\",\"type\":\"login\"}");

        RequestBody body = RequestBody.create(mediaType, "{\"deviceType\":\"Android\",\"webAuthUser\":\"" + namePost +"\",\"webAuthPassword\":\""+ passwordPost+"\",\"redirectUrl\":\"http://10.16.0.12:8081/?usermac=" + macPost + "&userip="+ ipPost +"&origurl=http://edge.microsoft.com/captiveportal/generate_204&nasip=10.100.0.1\",\"type\":\"login\"}");
        Log.d(TAG,"{\"deviceType\":\"Android\",\"webAuthUser\":\"" + namePost +"\",\"webAuthPassword\":\""+ passwordPost+"\",\"redirectUrl\":\"http://10.16.0.12:8081/?usermac=" + macPost + "&userip="+ ipPost +"&origurl=http://edge.microsoft.com/captiveportal/generate_204&nasip=10.100.0.1\",\"type\":\"login\"}");
        Request request = new Request.Builder()
                .url("http://10.16.0.12:8081/portal/api/v2/online?noCache=1605885991204")
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
                Log.d(TAG,e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                //返回200为登录正确 400为错误
                char checkLogin = responseData.toString().charAt(14);
                Log.d(TAG,responseData.toString());

                if (checkLogin == '2') {
                    Snackbar.make(coordinator, "登录成功啦", Snackbar.LENGTH_LONG)
//                            .setAction(action_text, click_listener)
                            .show();

                    Log.d(TAG, "登录成功啦");
                }
                else
                    Snackbar.make(coordinator, "登录失败惹", Snackbar.LENGTH_LONG)
//                            .setAction(action_text, click_listener)
                            .show();
                    Log.d(TAG,"登录失败惹");

            }
        });
    }


}
