package com.xiaomo.travelhelper.model;

/**
 * 用户相关常量集合
 */

public interface UserConst {


    String BASE_URL = "http://192.168.19.22:8080/";

    String BASE_USER_URL = BASE_URL + "api/user/";
    String ACCOUNT = "ACCOUNT";
    String PASSWORD = "PASSWORD";
    String TOKEN = "TOKEN";
    String IS_AUTO_LOGIN = "IS_AUTO_LOGIN";
    String USER_JSON = "USER_JSON";

    String UPDATE_NICKNAME = "UPDATE_NICKNAME";
    String UPDATE_ADDRESS = "UPDATE_ADDRESS";
    String UPDATE_TYPE_KEY = "UPDATE_TYPE_KEY";
    String UPDATE_BEFORE_VAL_KEY = "UPDATE_BEFORE_VAL_KEY";

}
