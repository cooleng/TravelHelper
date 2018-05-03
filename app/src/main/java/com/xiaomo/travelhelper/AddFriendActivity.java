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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xiaomo.travelhelper.http.FriendService;
import com.xiaomo.travelhelper.http.HttpServiceFactory;
import com.xiaomo.travelhelper.model.BaseResult;
import com.xiaomo.travelhelper.model.UserConst;
import com.xiaomo.travelhelper.model.chat.FriendResult;
import com.xiaomo.travelhelper.util.SharedPreferencesUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddFriendActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.add_friend__et) EditText mValEt;
    @BindView(R.id.add_friend__search_btn) Button mSearchBtn;
    @BindView(R.id.add_friend_back_iv) RelativeLayout mBackLayout;
    @BindView(R.id.add_friend__search_list_view) ListView mListView;

    private  FriendService friendService;
    private String account;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);

        initListener();
        account = SharedPreferencesUtil.read(this, UserConst.ACCOUNT,"");
        token = SharedPreferencesUtil.read(this, UserConst.TOKEN,"");
        friendService = HttpServiceFactory.buildFriendService();
    }

    private void initListener(){

        mSearchBtn.setOnClickListener(this);
        mBackLayout.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_friend__search_btn:
                String val = mValEt.getText().toString();
                friendService.searchFriend(account,token,val).subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<FriendResult>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(AddFriendActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onNext(FriendResult friendResult) {
                                if (friendResult != null && friendResult.isSuccess()) {
                                    List<FriendResult.User> mUserList = friendResult.getData();
                                    mListView.setAdapter(new MyAdapter(mUserList));
                                }else {
                                    Toast.makeText(AddFriendActivity.this,"无结果",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.add_friend_back_iv:
                finish();
                break;

        }
    }

    public class MyAdapter extends BaseAdapter{

        List<FriendResult.User> mUserList;

        public MyAdapter(List<FriendResult.User> userList) {

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
                view =  LayoutInflater.from(AddFriendActivity.this).inflate(R.layout.item_add_friend_layout,parent,false);
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
                    friendService.addFriend(account,token,mUserList.get(position).getAccount(),null).subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<BaseResult>() {
                                @Override
                                public void onCompleted() {
                                    v.setClickable(true);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(AddFriendActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    v.setClickable(true);
                                }

                                @Override
                                public void onNext(BaseResult baseResult) {
                                    if (baseResult != null) {
                                        Toast.makeText(AddFriendActivity.this,baseResult.getMsg(),Toast.LENGTH_SHORT).show();
                                        if(baseResult.isSuccess()){
                                            v.setVisibility(View.GONE);
                                        }
                                    }else {
                                        Toast.makeText(AddFriendActivity.this,"未知错误",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });
            return view;
        }


        public class ViewHolder{

            @BindView(R.id.add_friend_item_iv) ImageView imgIv;
            @BindView(R.id.add_friend_item_tv) TextView mNameTv;
            @BindView(R.id.add_friend_btn) Button mBtn;

            public ViewHolder(View view) {
                ButterKnife.bind(this,view);
            }
        }

    }

    public static void actionStart(Activity activity){
        Intent intent = new Intent(activity,AddFriendActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

    }




}
