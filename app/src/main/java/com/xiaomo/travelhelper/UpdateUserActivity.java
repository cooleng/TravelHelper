package com.xiaomo.travelhelper;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomo.travelhelper.http.HttpServiceFactory;
import com.xiaomo.travelhelper.http.UserService;
import com.xiaomo.travelhelper.model.UserConst;
import com.xiaomo.travelhelper.model.UserResult;
import com.xiaomo.travelhelper.util.SharedPreferencesUtil;
import com.xiaomo.travelhelper.util.UserUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UpdateUserActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.update_user_back_iv) RelativeLayout mBackLayout;
    @BindView(R.id.update_user_et) EditText mUpdateEt;
    @BindView(R.id.update_user_title_tv) TextView mTitleTv;
    @BindView(R.id.update_user_sure_btn) Button mSureBtn;

    private String type;
    private String val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        ButterKnife.bind(this);
        initListener();
        initData();
    }

    private void initData(){

        Intent intent = getIntent();
        type = intent.getStringExtra(UserConst.UPDATE_TYPE_KEY);
        val = intent.getStringExtra(UserConst.UPDATE_BEFORE_VAL_KEY);
        if(!TextUtils.isEmpty(type)){
            if(type.equals(UserConst.UPDATE_ADDRESS)){
                mTitleTv.setText("修改地区");
            }else if(type.equals(UserConst.UPDATE_NICKNAME)){
                mTitleTv.setText("修改昵称");
            }
        }
        mUpdateEt.setText(val);

    }

    private void initListener(){
        mBackLayout.setOnClickListener(this);
        mSureBtn.setOnClickListener(this);
    }

    public static void actionStart(Activity activity,String type,String val){
        Intent intent = new Intent(activity,UpdateUserActivity.class);
        intent.putExtra(UserConst.UPDATE_TYPE_KEY,type);
        intent.putExtra(UserConst.UPDATE_BEFORE_VAL_KEY,val);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update_user_back_iv:
                finish();
                break;
            case R.id.update_user_sure_btn:

                String account = SharedPreferencesUtil.read(UpdateUserActivity.this
                        , UserConst.ACCOUNT,"");
                String token = SharedPreferencesUtil.read(UpdateUserActivity.this
                        , UserConst.TOKEN,"");
                update(account,token,type);
                break;
        }
    }

    private void update(String account,String token,String type){
        mSureBtn.setClickable(false);
        UserService userService = HttpServiceFactory.buildUserService();
        Observable<UserResult> observable = null;
        if(type.equals(UserConst.UPDATE_ADDRESS)){
            observable = userService.update(account,token,null,mUpdateEt.getText().toString());
        }else if(type.equals(UserConst.UPDATE_NICKNAME)){
            observable = userService.update(account,token,mUpdateEt.getText().toString(),null);
        }
        if(observable != null){
            observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<UserResult>() {
                        @Override
                        public void onCompleted() {
                            mSureBtn.setClickable(true);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(UpdateUserActivity.this,e.getMessage()
                                    ,Toast.LENGTH_SHORT).show();
                            mSureBtn.setClickable(true);
                        }

                        @Override
                        public void onNext(UserResult userResult) {
                            if(userResult != null && userResult.isSuccess()){
                                UserUtil.saveUserResult(UpdateUserActivity.this,userResult);
                                finish();
                            }else if(userResult != null){
                                Toast.makeText(UpdateUserActivity.this,userResult.getMsg()
                                        ,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

}
