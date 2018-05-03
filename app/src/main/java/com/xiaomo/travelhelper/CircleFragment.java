package com.xiaomo.travelhelper;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaomo.travelhelper.dao.PrivateChatDao;
import com.xiaomo.travelhelper.model.UserConst;
import com.xiaomo.travelhelper.model.chat.PrivateChatModel;
import com.xiaomo.travelhelper.util.ImageUtils;
import com.xiaomo.travelhelper.util.SharedPreferencesUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.imallan.jellyrefresh.JellyRefreshLayout;
import uk.co.imallan.jellyrefresh.PullToRefreshLayout;

/**
 * 朋友圈 Fragment 页面
 */

public class CircleFragment extends Fragment implements View.OnClickListener{

    private Context mContext;
    private Fragment mFragment;
    private View mView;
    @BindView(R.id.circle_refresh_layout) JellyRefreshLayout mJellyRefreshLayout;
    @BindView(R.id.friend_circle_layout) LinearLayout mFriendCircleLayout;
    @BindView(R.id.chat_msg_list_view) ListView mListView;

    private PrivateChatDao mPrivateChatDao;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mFragment = this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_circle_layout,container,false);

        ButterKnife.bind(this,mView);

        mJellyRefreshLayout.setPullToRefreshListener(new PullToRefreshLayout.PullToRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                pullToRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mJellyRefreshLayout.setRefreshing(false);
                    }
                }, 500);

            }
        });
        mFriendCircleLayout.setOnClickListener(this);
        init();
        return mView;

    }

    private void init(){
        mPrivateChatDao = new PrivateChatDao();
    }

    @Override
    public void onResume() {
        super.onResume();

        List<PrivateChatModel> resultList =  mPrivateChatDao.findFirstGroup(SharedPreferencesUtil.read(
                mContext, UserConst.ACCOUNT,""));
        Log.i("ChatService","分组查询未读消息的用户账号 - " + resultList);
        if(resultList != null && !resultList.isEmpty()){
            mListView.setAdapter(new ChatMsgAdapter(resultList));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.friend_circle_layout:
                ShareNewsActivity.actionStart((AppCompatActivity) mContext);
                break;
        }
    }


    class ChatMsgAdapter extends BaseAdapter{

        private List<PrivateChatModel> resultList;

        public ChatMsgAdapter(List<PrivateChatModel> resultList) {
            this.resultList = resultList;
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public Object getItem(int position) {
            return resultList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if(convertView == null){
                convertView = LayoutInflater.from(mContext)
                        .inflate(R.layout.item_chat_msg_list,parent,false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final String account = resultList.get(position).getFromAccount();
            final String username = resultList.get(position).getFromUsername();
            final String url = ImageUtils.buildImgUrl(resultList.get(position).getFromImg());
            viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrivateChatActivity.actionStart((AppCompatActivity) mContext,account,username,url);
                }
            });

            Glide.with(mContext).load(url).into(viewHolder.mImageView);
            viewHolder.mTextView.setText(username);

            return convertView;
        }

        class ViewHolder{

            @BindView(R.id.chat_msg_item_layout) LinearLayout mLayout;
            @BindView(R.id.chat_msg_item_iv) ImageView mImageView;
            @BindView(R.id.chat_msg_item_username_tv) TextView mTextView;

            public ViewHolder(View view) {

                ButterKnife.bind(this,view);

            }
        }


    }


}
