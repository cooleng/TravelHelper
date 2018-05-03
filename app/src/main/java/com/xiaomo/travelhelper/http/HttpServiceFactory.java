package com.xiaomo.travelhelper.http;

import com.xiaomo.travelhelper.model.UserConst;
import com.xiaomo.travelhelper.model.found.FoundContants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 请求服务类工厂
 */

public class HttpServiceFactory {

    public static WeatherService buildWeatherService(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FoundContants.HE_WEATHER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(WeatherService.class);
    }

    public static UserService buildUserService(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserConst.BASE_USER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(UserService.class);
    }

    public static FriendService buildFriendService(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FriendService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(FriendService.class);
    }

    public static MsgService buildMsgService(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MsgService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(MsgService.class);
    }


    public static ShareService buildShareService(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ShareService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(ShareService.class);
    }

    public static UploadService buildUploadService(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UploadService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(UploadService.class);
    }

    public static IChatService buildChatService(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IChatService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(IChatService.class);
    }



}
