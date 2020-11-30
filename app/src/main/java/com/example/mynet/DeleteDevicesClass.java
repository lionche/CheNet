package com.example.mynet;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.mynet.javabean.DevicesInfoBean;
import com.example.mynet.javabean.DevicesRequestBean;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.example.mynet.LoginPostClass.loginResponseBean;

public class DeleteDevicesClass {

    static String TAG = "testhttp";

    public static void DeleteDevices(String deviceID){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("http://10.16.0.12:8081/portal/api/v2/session/acctUniqueId/"+deviceID)
                .method("DELETE", body)
                .addHeader("accept", "*/*")
                .addHeader("accept-encoding", "gzip, deflate")
                .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6")
                .addHeader("authorization", loginResponseBean.getToken())
                .addHeader("content-type", "application/json")
                .addHeader("dnt", "1")
                .addHeader("host", "10.16.0.12:8081")
                .addHeader("origin", "http://10.16.0.12:8081")
                .addHeader("proxy-connection", "keep-alive")
                .addHeader("referer", "http://10.16.0.12:8081/deviceList")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.67 Safari/537.36 Edg/87.0.664.47")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "onResponse删除设备 : "+responseData);
            }
        });

    }
}
