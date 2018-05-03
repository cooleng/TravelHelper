package com.xiaomo.travelhelper.util;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.xiaomo.travelhelper.http.ChatService;
import com.xiaomo.travelhelper.http.HttpServiceFactory;
import com.xiaomo.travelhelper.http.UserService;
import com.xiaomo.travelhelper.model.UserConst;
import com.xiaomo.travelhelper.model.UserResult;

import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by mojiale on 2018/4/9.
 */

public class UserUtil {

    private static UserResult mUserResult;

    public static void saveUserResult(Context context, UserResult userResult){
        if(userResult == null){
            return;
        }
        mUserResult = userResult;
        Gson gson = new Gson();
        String json = gson.toJson(userResult);
        SharedPreferencesUtil.save(context, UserConst.USER_JSON,json);
    }

    public static UserResult getUserResult(Context context){

        if(mUserResult != null){
            return mUserResult;
        }
        String json = SharedPreferencesUtil.read(context, UserConst.USER_JSON,"");
        if(!TextUtils.isEmpty(json)){
            Gson gson = new Gson();
            return gson.fromJson(json,UserResult.class);
        }
        return null;
    }

    public static void getAndSaveUserInfo(final Context context, String account, String token){

        UserService userService = HttpServiceFactory.buildUserService();
        userService.getInfo(account,token).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<UserResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(UserResult userResult) {
                        if(userResult != null && userResult.isSuccess()){
                            saveUserResult(context,userResult);

                            // 上线连接
                            try{
                                ChatService.online(userResult.getData().getAccount()
                                        ,userResult.getData().getUsername());
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    }
                });


    }
}
