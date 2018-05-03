package com.xiaomo.travelhelper;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xiaomo.travelhelper.http.FriendService;
import com.xiaomo.travelhelper.http.HttpServiceFactory;
import com.xiaomo.travelhelper.model.UserConst;
import com.xiaomo.travelhelper.model.chat.FriendResult;
import com.xiaomo.travelhelper.util.ImageUtils;
import com.xiaomo.travelhelper.util.SharedPreferencesUtil;
import com.xiaomo.travelhelper.view.contact.ISideBarSelectCallBack;
import com.xiaomo.travelhelper.view.contact.SideBar;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.imallan.jellyrefresh.JellyRefreshLayout;
import uk.co.imallan.jellyrefresh.PullToRefreshLayout;

/**
 * 好友 Fragment 页面
 */

public class FriendFragment extends Fragment{

    private Context mContext;
    private Fragment mFragment;
    private View mView;
    @BindView(R.id.friend_list_view) ListView mListView;
    @BindView(R.id.friend_side_bar) SideBar mSideBar;
    @BindView(R.id.friend_refresh_layout) JellyRefreshLayout mRefreshLayout;

    private List<FriendResult.User> mUserList;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        mFragment = this;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_friend_layout,container,false);
        ButterKnife.bind(this,mView);
        mSideBar.setOnStrSelectCallBack(new ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                for (int i = 0; i < mUserList.size(); i++) {
                    if (selectStr.equalsIgnoreCase(mUserList.get(i).getFirstLetter())) {
                        mListView.setSelection(i); // 选择到首字母出现的位置
                        return;
                    }
                }
            }
        });
        if(mUserList != null && !mUserList.isEmpty()){
            mListView.setAdapter(new FriendAdapter(mContext,mUserList));
        }else{
            getFriends();
        }
        initRefreshLayout();
        return mView;
    }

    private void initRefreshLayout(){
        TextView tv = new TextView(mContext);
        tv.setText("刷新中");
        tv.setTextColor(getResources().getColor(R.color.grey_2));
        tv.setTextSize(18);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        mRefreshLayout.setLoadingView(tv);
        mRefreshLayout.setPullToRefreshListener(new PullToRefreshLayout.PullToRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                pullToRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                        getFriends();
                    }
                }, 500);

            }
        });
    }

    private void  getFriends(){

        FriendService friendService = HttpServiceFactory.buildFriendService();
        String account = SharedPreferencesUtil.read(mContext, UserConst.ACCOUNT,"");
        String token = SharedPreferencesUtil.read(mContext, UserConst.TOKEN,"");
        friendService.listFriends(account,token).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FriendResult>() {
            @Override
            public void onCompleted() {
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(FriendResult friendResult) {
                if(friendResult != null && friendResult.isSuccess()){
                    mUserList = friendResult.getData();
                    initPinyin(mUserList);
                    Collections.sort(mUserList);
                    mListView.setAdapter(new FriendAdapter(mContext,mUserList));
                }
            }
        });

    }


    private void initPinyin(List<FriendResult.User> userList){

        if(userList != null){
            for(FriendResult.User user : userList){
                user.initPinyin();
            }
        }


    }


    public class FriendAdapter extends BaseAdapter{

        private List<FriendResult.User> list = null;
        private Context context;

        public FriendAdapter(Context context,List<FriendResult.User> list) {
            this.context = context;
            this.list = list;

        }

        @Override
        public int getCount() {
            if(list == null){
                return 0;
            }
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {

            ViewHolder viewHolder;
            if(view == null){
                view =  LayoutInflater.from(context).inflate(R.layout.item_friend_list,parent,false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) view.getTag();
            }

            //根据position获取首字母作为目录catalog
            String catalog = list.get(position).getFirstLetter();
            FriendResult.User user = list.get(position);

            //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if(position == getPositionForSection(catalog)){
                viewHolder.catalogTv.setVisibility(View.VISIBLE);
                viewHolder.catalogTv.setText(user.getFirstLetter().toUpperCase());
            }else{
                viewHolder.catalogTv.setVisibility(View.GONE);
            }

            viewHolder.nameTv.setText(this.list.get(position).getUsername());
            String img = this.list.get(position).getImg();
            if(!TextUtils.isEmpty(img)){
                Glide.with(mContext).load(ImageUtils.buildImgUrl(img)).into(viewHolder.headIv);
            }

            viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击跳转
                    String targetAccount = list.get(position).getAccount();
                    String targetUsername = list.get(position).getUsername();
                    String imgUrl = list.get(position).getImg();
                    PrivateChatActivity.actionStart((AppCompatActivity) mContext
                            ,targetAccount,targetUsername,imgUrl);
                }
            });

            return view;

        }


        class ViewHolder {

            @BindView(R.id.friend_item_iv) ImageView headIv;
            @BindView(R.id.catalog_tv) TextView catalogTv;
            @BindView(R.id.friend_item_tv) TextView nameTv;
            @BindView(R.id.friend_item_layout) LinearLayout mLayout;

            ViewHolder(View v){
                ButterKnife.bind(this,v);
            }

        }


        /**
         * 获取catalog首次出现位置
         */
        public int getPositionForSection(String catalog) {
            for (int i = 0; i < getCount(); i++) {
                String sortStr = list.get(i).getFirstLetter();
                if (catalog.equalsIgnoreCase(sortStr)) {
                    return i;
                }
            }
            return -1;
        }


    }








}
