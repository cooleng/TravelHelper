package com.xiaomo.travelhelper;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xiaomo.travelhelper.http.FriendService;
import com.xiaomo.travelhelper.http.HttpServiceFactory;
import com.xiaomo.travelhelper.http.MsgService;
import com.xiaomo.travelhelper.model.BaseResult;
import com.xiaomo.travelhelper.model.UserConst;
import com.xiaomo.travelhelper.model.chat.MsgFriendResult;
import com.xiaomo.travelhelper.util.SharedPreferencesUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AgreeFriendActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.agree_friend_back_iv) RelativeLayout mBackLayout;
    @BindView(R.id.agree_friend__search_list_view) ListView mListView;

    private FriendService friendService;
    private MsgService mMsgService;
    private String account;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree_friend);
        ButterKnife.bind(this);

        initListener();
        account = SharedPreferencesUtil.read(this, UserConst.ACCOUNT,"");
        token = SharedPreferencesUtil.read(this, UserConst.TOKEN,"");
        friendService = HttpServiceFactory.buildFriendService();
        mMsgService = HttpServiceFactory.buildMsgService();

        listMsgFriend();
    }

    private void initListener(){

        mBackLayout.setOnClickListener(this);

    }


    private void listMsgFriend(){
        mMsgService.listMsgFriend(account,token).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MsgFriendResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AgreeFriendActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(MsgFriendResult msgFriendResult) {
                        if (msgFriendResult != null && msgFriendResult.isSuccess()) {
                            mListView.setAdapter(new MyAdapter(msgFriendResult.getData()));
                        }else {
                            Toast.makeText(AgreeFriendActivity.this,"无结果",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.agree_friend_back_iv:
                finish();
                break;

        }
    }

    public class MyAdapter extends BaseAdapter {

        List<MsgFriendResult.DataEntity> mUserList;

        public MyAdapter(List<MsgFriendResult.DataEntity> userList) {

            this.mUserList = userList;

        }

        @Override
        public int getCount() {
            if(mUserList == null){
                return 0;
            }
            return mUserList.size();
        }

        @Override
        public Object getItem(int position) {
            return mUserList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {

            ViewHolder viewHolder;
            if(view == null){
                view =  LayoutInflater.from(AgreeFriendActivity.this).inflate(R.layout.item_agree_friend_layout,parent,false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.mNameTv.setText(mUserList.get(position).getUsername());
            String img = mUserList.get(position).getImg();
            if(!TextUtils.isEmpty(img)){
                Glide.with(viewHolder.imgIv).load(img);
            }
            viewHolder.mBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    v.setClickable(false);
                    friendService.agreeFriend(account,token,mUserList.get(position).getAccount()).subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<BaseResult>() {
                                @Override
                                public void onCompleted() {
                                    v.setClickable(true);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(AgreeFriendActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    v.setClickable(true);
                                }

                                @Override
                                public void onNext(BaseResult baseResult) {
                                    if (baseResult != null) {
                                        Toast.makeText(AgreeFriendActivity.this,baseResult.getMsg(),Toast.LENGTH_SHORT).show();
                                        if(baseResult.isSuccess()){
                                            v.setVisibility(View.GONE);
                                        }
                                    }else {
                                        Toast.makeText(AgreeFriendActivity.this,"未知错误",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });
            return view;
        }


        public class ViewHolder{

            @BindView(R.id.agree_friend_item_iv)
            ImageView imgIv;
            @BindView(R.id.agree_friend_item_tv)
            TextView mNameTv;
            @BindView(R.id.agree_friend_btn)
            Button mBtn;

            public ViewHolder(View view) {
                ButterKnife.bind(this,view);
            }
        }

    }

    public static void actionStart(Activity activity){
        Intent intent = new Intent(activity,AgreeFriendActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

    }

}
