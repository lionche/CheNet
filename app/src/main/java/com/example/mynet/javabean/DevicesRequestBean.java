package com.example.mynet.javabean;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DevicesRequestBean {

    /**
     * concurrency : 2
     * sessions : [{"deviceType":"Android","experienceEndTime":0,"assignment":"HUAWEI TECHNOLOGIES CO.,LTD","user_name":"2017118069","acct_session_id":"000000072020113008063901746a482048100400","nas_ip_address":"10.100.0.1","framed_ip_address":"10.21.80.226","calling_station_id":"EC-56-23-7C-97-A3","acct_start_time":"2020-11-30 16:06:39","acct_unique_id":"radius:acct:3f9f595c-0a55-490f-baed-6dfad7213467:xx:35380391c991d0e8ff6c4b51f6ffb41a"},{"deviceType":"PC","experienceEndTime":0,"assignment":"","user_name":"2017118069","acct_session_id":"0000000720201129235649017343f92048100400","nas_ip_address":"10.100.0.1","framed_ip_address":"10.18.132.35","calling_station_id":"66-F8-6A-8B-53-EC","acct_start_time":"2020-11-30 07:56:49","acct_unique_id":"radius:acct:3f9f595c-0a55-490f-baed-6dfad7213467:xx:46ff08ff479cf1c37fa677cd4fa83935"}]
     */

    private String concurrency;
    private List<SessionsBean> sessions;

    @NoArgsConstructor
    @Data
    public static class SessionsBean {
        /**
         * deviceType : Android
         * experienceEndTime : 0
         * assignment : HUAWEI TECHNOLOGIES CO.,LTD
         * user_name : 2017118069
         * acct_session_id : 000000072020113008063901746a482048100400
         * nas_ip_address : 10.100.0.1
         * framed_ip_address : 10.21.80.226
         * calling_station_id : EC-56-23-7C-97-A3
         * acct_start_time : 2020-11-30 16:06:39
         * acct_unique_id : radius:acct:3f9f595c-0a55-490f-baed-6dfad7213467:xx:35380391c991d0e8ff6c4b51f6ffb41a
         */

        private String deviceType;
        private Integer experienceEndTime;
        private String assignment;
        private String user_name;
        private String acct_session_id;
        private String nas_ip_address;
        private String framed_ip_address;
        private String calling_station_id;
        private String acct_start_time;
        private String acct_unique_id;
    }
}
