package com.xiaomo.travelhelper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomo.travelhelper.http.HttpServiceFactory;
import com.xiaomo.travelhelper.http.UserService;
import com.xiaomo.travelhelper.model.BaseResult;
import com.xiaomo.travelhelper.model.UserConst;
import com.xiaomo.travelhelper.model.UserResult;
import com.xiaomo.travelhelper.util.SharedPreferencesUtil;
import com.xiaomo.travelhelper.util.UserUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 备忘录 Fragment 页面
 */

public class MemoFragment extends Fragment implements View.OnClickListener{

    // fragment 中的 rootView
    private View mView;
    private Context mContext;
    private Fragment mFragment;

    @BindView(R.id.my_memo_layout) LinearLayout memoLayout;
    @BindView(R.id.my_msg_layout) LinearLayout msgLayout;
    @BindView(R.id.memo_head_iv) ImageView mHeadIV;
    @BindView(R.id.memo_nickname_tv) TextView mNickNameTv;
    @BindView(R.id.memo_account_tv) TextView mAccountTv;
    @BindView(R.id.memo_address_tv) TextView mAddressTv;
    @BindView(R.id.memo_exit_layout) LinearLayout mExitLayout;
    @BindView(R.id.memo_nickname_layout) LinearLayout mNickNameLayout;
    @BindView(R.id.memo_address_layout) LinearLayout mAddressLayout;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        mFragment = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = LayoutInflater.from(mContext).inflate(R.layout.fragment_memo_layout,container,false);
        ButterKnife.bind(this,mView);
        initListener();
        return mView;

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initListener(){
        memoLayout.setOnClickListener(this);
        mNickNameLayout.setOnClickListener(this);
        mAddressLayout.setOnClickListener(this);
        mExitLayout.setOnClickListener(this);
        msgLayout.setOnClickListener(this);
    }

    private void initData(){
        UserResult userResult = UserUtil.getUserResult(mContext);
        if(userResult == null){
            String account = SharedPreferencesUtil.read(mContext, UserConst.ACCOUNT,"");
            String token = SharedPreferencesUtil.read(mContext, UserConst.TOKEN,"");
            getUserInfo(account,token);
        }else{
            mNickNameTv.setText(userResult.getData().getUsername());
            mAccountTv.setText(userResult.getData().getAccount());
            mAddressTv.setText(userResult.getData().getArea());
        }
    }


    public void getUserInfo(String account, String token){

        UserService userService = HttpServiceFactory.buildUserService();
        userService.getInfo(account,token).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(UserResult userResult) {
                        if(userResult != null && userResult.isSuccess()){
                            mNickNameTv.setText(userResult.getData().getUsername());
                            mAccountTv.setText(userResult.getData().getAccount());
                            mAddressTv.setText(userResult.getData().getArea());
                            UserUtil.saveUserResult(mContext,userResult);
                        }else if(userResult != null){
                            Toast.makeText(mContext,userResult.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.memo_exit_layout:
                logout();
                break;
            case R.id.my_memo_layout:
                MemoActivity.actionStart((AppCompatActivity) mContext);
                break;
            case R.id.memo_address_layout:
                UpdateUserActivity.actionStart((Activity) mContext
                        , UserConst.UPDATE_ADDRESS,mAddressTv.getText().toString());
                break;
            case R.id.memo_nickname_layout:
                UpdateUserActivity.actionStart((Activity) mContext
                        , UserConst.UPDATE_NICKNAME,mNickNameTv.getText().toString());
                break;
            case R.id.my_msg_layout:
                AgreeFriendActivity.actionStart((Activity) mContext);
                break;
        }

    }

    private void logout(){
        SharedPreferencesUtil.save(mContext, UserConst.IS_AUTO_LOGIN,false);
        UserService userService = HttpServiceFactory.buildUserService();
        userService.logout(mAccountTv.getText().toString()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResult>() {
                    @Override
                    public void onCompleted() {
                        LoginActivity.actionStart((Activity) mContext);
                        ((Activity) mContext).finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoginActivity.actionStart((Activity) mContext);
                        ((Activity) mContext).finish();
                    }

                    @Override
                    public void onNext(BaseResult baseResult) {

                    }
                });
    }
}
