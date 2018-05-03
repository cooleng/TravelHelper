package com.xiaomo.travelhelper.http;


import com.xiaomo.travelhelper.model.LoginResult;
import com.xiaomo.travelhelper.model.BaseResult;
import com.xiaomo.travelhelper.model.UserResult;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 用户模块接口
 */

public interface UserService {

    @POST("login")
    Observable<LoginResult> login(@Query("account") String account,@Query("password") String password);

    @POST("register")
    Observable<UserResult> register(@Query("account") String account, @Query("password") String password);

    @POST("logout")
    Observable<BaseResult> logout(@Query("account") String account);

    @POST("isOnLine")
    Observable<BaseResult> isOnLine(@Query("account") String account);

    @POST("getInfo")
    Observable<UserResult> getInfo(@Query("account") String account, @Query("token") String token);

    @POST("update")
    Observable<UserResult> update(@Query("account") String account, @Query("token") String token
            ,@Query("username") String username,@Query("area") String area);





}
