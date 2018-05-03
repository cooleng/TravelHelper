package com.xiaomo.travelhelper.http;


import com.xiaomo.travelhelper.model.found.CurrentAir;
import com.xiaomo.travelhelper.model.found.CurrentWeather;
import com.xiaomo.travelhelper.model.found.ForecastWeather;
import com.xiaomo.travelhelper.model.found.LifeStyle;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 请求天气接口
 */

public interface WeatherService {

    @GET("weather/now?key=71080c38500c4aaaab480adfc1080f41")
    Observable<CurrentWeather> getCurrentWeatherByLatLng(@Query("location") String latLng);

    @GET("weather/forecast?key=71080c38500c4aaaab480adfc1080f41")
    Observable<ForecastWeather> getForecastWeatherByLatLng(@Query("location") String latLng);

    @GET("weather/lifestyle?key=71080c38500c4aaaab480adfc1080f41")
    Observable<LifeStyle> getLifeStyleByLatLng(@Query("location") String latLng);

    @GET("air/now?key=71080c38500c4aaaab480adfc1080f41")
    Observable<CurrentAir> getCurrentAirByLatLng(@Query("location") String latLng);


}
