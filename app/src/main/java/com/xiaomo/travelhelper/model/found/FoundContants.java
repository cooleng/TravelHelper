package com.xiaomo.travelhelper.model.found;

/**
 * 发现模块常量类
 */

public interface FoundContants {

    /**
     * 位置
     */
    int REQUEST_BASE = 1;
    /**
     * 导航
     */
    int REQUEST_ROTE = 2;

    /**
     * 周边
     */
    int  REQUEST_AROUND = 3;

    String REQUEST_KEY = "BAI_DU_MAP";


    // 和风天气
    String HE_WEATHER_KEY = "71080c38500c4aaaab480adfc1080f41";
    String HE_WEATHER_USER_ID = "HE1703061553001827";
    String HE_WEATHER_BASE_URL = "https://free-api.heweather.com/s6/";
}
