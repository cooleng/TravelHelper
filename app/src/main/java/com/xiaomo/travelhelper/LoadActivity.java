package com.xiaomo.travelhelper;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.xiaomo.travelhelper.http.HttpServiceFactory;
import com.xiaomo.travelhelper.http.UserService;
import com.xiaomo.travelhelper.model.BaseResult;
import com.xiaomo.travelhelper.model.LoginResult;
import com.xiaomo.travelhelper.model.UserConst;
import com.xiaomo.travelhelper.util.SharedPreferencesUtil;
import com.xiaomo.travelhelper.util.UserUtil;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoadActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        // 百度SDK初始化
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.GCJ02);

        new Handler().postDelayed(new Runnable(){
            public void run() {
               handleLogin();
            }
        }, 2000);

    }

    private void handleLogin(){
        boolean isAutoLogin = SharedPreferencesUtil.read(LoadActivity.this, UserConst.IS_AUTO_LOGIN,false);
        if(isAutoLogin){
            // 登录
            final String account = SharedPreferencesUtil.read(LoadActivity.this, UserConst.ACCOUNT,"");
            final String password = SharedPreferencesUtil.read(LoadActivity.this, UserConst.PASSWORD,"");
            final UserService userService = HttpServiceFactory.buildUserService();
            userService.isOnLine(account).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<BaseResult>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            LoginActivity.actionStart(LoadActivity.this);
                            finish();
                        }

                        @Override
                        public void onNext(BaseResult baseResult) {
                            if(baseResult != null && baseResult.isSuccess()){
                                MainActivity.actionStart(LoadActivity.this);
                                finish();
                            }else{
                                login(account,password);
                            }
                        }
                    });
        }else{
            // 跳转登录页面
            LoginActivity.actionStart(LoadActivity.this);
            finish();
        }
    }



    private void login(final String account,final String password){

        final UserService userService = HttpServiceFactory.buildUserService();
        userService.login(account,password).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginResult>() {
                    @Override
                    public void onCompleted() {
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(LoadActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        LoginActivity.actionStart(LoadActivity.this);
                        finish();
                    }

                    @Override
                    public void onNext(LoginResult loginResult) {
                        if(loginResult != null && loginResult.isSuccess()){
                            SharedPreferencesUtil.save(LoadActivity.this, UserConst.ACCOUNT,account);
                            SharedPreferencesUtil.save(LoadActivity.this, UserConst.PASSWORD,password);
                            SharedPreferencesUtil.save(LoadActivity.this, UserConst.TOKEN,loginResult.getData());
                            SharedPreferencesUtil.save(LoadActivity.this, UserConst.IS_AUTO_LOGIN,true);
                            UserUtil.getAndSaveUserInfo(LoadActivity.this,account,loginResult.getData());
                            MainActivity.actionStart(LoadActivity.this);
                        }else {
                            LoginActivity.actionStart(LoadActivity.this);
                        }
                    }
                });
    }

}
