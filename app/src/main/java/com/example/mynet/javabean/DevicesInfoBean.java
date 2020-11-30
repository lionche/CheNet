package com.example.mynet.javabean;

import lombok.Data;

@Data
public class DevicesInfoBean {
    //设备类型
    private String deviceType;
    //设备IP
    private String framed_ip_address;
    //设备MAC
    private String calling_station_id;
    //设备连接时间
    private String acct_start_time;
    //设备ID
    private String acct_unique_id;

}
