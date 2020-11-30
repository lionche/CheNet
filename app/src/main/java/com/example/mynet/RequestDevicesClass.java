package com.example.mynet;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.mynet.javabean.DevicesInfoBean;
import com.example.mynet.javabean.DevicesRequestBean;
import com.example.mynet.javabean.LoginPostBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.example.mynet.LoginPostClass.loginResponseBean;

public class RequestDevicesClass {
    static String TAG = "testhttp";

    public static DevicesInfoBean devicesInfoBean1;

//    static ArrayList devicesInfoBeanList = new ArrayList();
    static List<DevicesInfoBean> devicesInfoBeanArrayList = new ArrayList<>();



    public static void RequestDevices() {


        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(3, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url("http://10.16.0.12:8081/portal/api/v2/session/list")
                .method("GET", null)
                .addHeader("accept", "*/*")
                .addHeader("accept-encoding", "gzip, deflate")
                .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6")
                .addHeader("authorization", loginResponseBean.getToken())
                .addHeader("content-type", "application/json")
//                .addHeader("cookie", "redirectUrl=http://10.16.0.12:8081/?usermac=2C-61-04-FF-73-68&userip=10.22.193.12&origurl=http://s3.cn-northwest-1.amazonaws.com.cn/captive-portal/connection-test.html?noCache=1606580561813442782&nasip=10.100.0.1; token=d258e916aa384de449a489e2abf0642e394dc69548cdbdda602fa64a6f3469de083c110756e9cefdf86e19278d3548db3a4734a68e418630")
                .addHeader("dnt", "1")
                .addHeader("host", "10.16.0.12:8081")
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

                DevicesRequestBean devicesRequestBean = JSON.parseObject(responseData, DevicesRequestBean.class);

                Log.d(TAG, "查询设备的json: " + devicesRequestBean.getSessions());
                Log.d(TAG, "查询到的设备数量: " + devicesRequestBean.getSessions().size());
                devicesInfoBeanArrayList.clear();

                for (int i = 0; i < devicesRequestBean.getSessions().size(); i++) {
                    DevicesRequestBean.SessionsBean sessionsBean = devicesRequestBean.getSessions().get(i);
                    DevicesInfoBean devicesInfoBean = setSessionsBean(sessionsBean);

                    devicesInfoBeanArrayList.add(i, devicesInfoBean);

                    Log.d(TAG, "查询的设备保存为" + devicesInfoBean.toString());
                    Log.d(TAG, "onResponse: 保存的设备名字为"+devicesInfoBean.getDeviceType());
                    Log.d(TAG, "onResponse: 保存的设备id为"+devicesInfoBean.getAcct_unique_id());

                }

/*                DevicesRequestBean.SessionsBean sessionsBean = devicesRequestBean.getSessions().get(1);
                devicesInfoBean1 = new DevicesInfoBean();
                devicesInfoBean1.setDeviceType(sessionsBean.getDeviceType());
                devicesInfoBean1.setFramed_ip_address(sessionsBean.getFramed_ip_address());
                devicesInfoBean1.setCalling_station_id(sessionsBean.getCalling_station_id());
                devicesInfoBean1.setAcct_start_time(sessionsBean.getAcct_start_time());
                devicesInfoBean1.setAcct_unique_id(sessionsBean.getAcct_unique_id());*/

            }


        });

    }

    private static DevicesInfoBean setSessionsBean(DevicesRequestBean.SessionsBean sessionsBean) {
        DevicesInfoBean devicesInfoBean = new DevicesInfoBean();
        devicesInfoBean.setDeviceType(sessionsBean.getDeviceType());
        devicesInfoBean.setFramed_ip_address(sessionsBean.getFramed_ip_address());
        devicesInfoBean.setCalling_station_id(sessionsBean.getCalling_station_id());
        devicesInfoBean.setAcct_start_time(sessionsBean.getAcct_start_time());
        devicesInfoBean.setAcct_unique_id(sessionsBean.getAcct_unique_id());
        return devicesInfoBean;

    }


}
