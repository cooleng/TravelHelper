package com.xiaomo.travelhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xiaomo.travelhelper.dao.PrivateChatDao;
import com.xiaomo.travelhelper.http.ChatService;
import com.xiaomo.travelhelper.model.UserConst;
import com.xiaomo.travelhelper.model.UserResult;
import com.xiaomo.travelhelper.model.chat.PrivateChatModel;
import com.xiaomo.travelhelper.model.chat.PrivateChatRequestDTO;
import com.xiaomo.travelhelper.util.ImageUtils;
import com.xiaomo.travelhelper.util.SharedPreferencesUtil;
import com.xiaomo.travelhelper.util.UserUtil;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivateChatActivity extends AppCompatActivity implements View.OnClickListener{

    private String targetAccount;
    private String targetUsername;
    private String targetImgUrl;
    private String account;
    private String imgUrl;
    private String username;

    @BindView(R.id.private_chat_back_iv) RelativeLayout mBackIv;
    @BindView(R.id.private_chat_title_tv) TextView mTitleTv;
    @BindView(R.id.chat_recycler_view)  RecyclerView mRecyclerView;
    @BindView(R.id.chat_et)  EditText mChatET;
    @BindView(R.id.chat_send_btn)  Button mSendBtn;

    private PrivateChatDao mPrivateChatDao;
    private ChatAdapter mChatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_chat);
        ButterKnife.bind(this);
        initData();
        initView();
        initScanner();
    }

    private void initData(){

        Intent intent = getIntent();
        targetAccount =  intent.getStringExtra("targetAccount");
        targetUsername = intent.getStringExtra("targetUsername");
        targetImgUrl = intent.getStringExtra("targetImgUrl");

        account = SharedPreferencesUtil.read(PrivateChatActivity.this, UserConst.ACCOUNT,"");
        mPrivateChatDao = new PrivateChatDao();

        UserResult userResult = UserUtil.getUserResult(PrivateChatActivity.this);
        imgUrl = userResult.getData().getImg();
        username = userResult.getData().getUsername();
    }


    public static void actionStart(AppCompatActivity context,String targetAccount,String targetUsername
            ,String imgUrl){

        Intent intent = new Intent(context,PrivateChatActivity.class);
        intent.putExtra("targetAccount",targetAccount);
        intent.putExtra("targetUsername",targetUsername);
        intent.putExtra("targetImgUrl",imgUrl);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    private void initView(){

        mTitleTv.setText(targetUsername);
        mSendBtn.setOnClickListener(this);
        mBackIv.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PrivateChatActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);

        List<PrivateChatModel> resultList = mPrivateChatDao.listUnreadByFromAndTo(targetAccount,account);
        Log.i("ChatService","获取未读消息-" + resultList);
        mPrivateChatDao.setRead(resultList);
        if(resultList == null || resultList.isEmpty()){
            resultList = mPrivateChatDao.listReadByFromAndTo(targetAccount,account);
            Log.i("ChatService","获取已读消息-" + resultList);
        }
        mChatAdapter = new ChatAdapter(resultList);
        mRecyclerView.setAdapter(mChatAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chat_send_btn:

                String content = mChatET.getText().toString();
                if(!TextUtils.isEmpty(content)){
                    // 发送数据
                    PrivateChatRequestDTO requestDTO = new PrivateChatRequestDTO();
                    requestDTO.setModule((short)1);
                    requestDTO.setCmd((short)1);
                    requestDTO.setContent(content);
                    requestDTO.setSendAccount(account);
                    requestDTO.setTargetAccount(targetAccount);
                    try{
                        ChatService.send(requestDTO);
                        PrivateChatModel model = new PrivateChatModel();
                        model.setFlag(1);
                        model.setContent(content);
                        model.setFromAccount(account);
                        model.setFromUsername(username);
                        model.setFromImg(imgUrl);
                        model.setToAccount(targetAccount);
                        model.setToUsername(targetUsername);
                        model.setToImg(targetImgUrl);
                        model.setTime(new Date(System.currentTimeMillis()));
                        model.save();

                        Log.i("ChatService","保存数据-" + model);

                        mChatAdapter.addData(model);
                        mChatET.setText("");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.private_chat_back_iv:
                finish();
                break;
        }
    }

    private Timer mTimer;

    private void initScanner(){

        // 每5秒扫描一次
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i("ChatService","扫描未读信息-" + targetAccount+"-" + account);
                final List<PrivateChatModel> modelList = mPrivateChatDao.listUnreadByFromAndTo(targetAccount,account);
                Log.i("ChatService","结果-" + modelList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mChatAdapter.addData(modelList);
                    }
                });
                mPrivateChatDao.setRead(modelList);
            }
        },1000,5*1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mTimer.cancel();

    }

    class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

        private List<PrivateChatModel> resultList;

        public ChatAdapter(List<PrivateChatModel> resultList) {
            this.resultList = resultList;
        }

        public void updateData(List<PrivateChatModel> resultList){

            this.resultList = resultList;
            notifyDataSetChanged();
            mRecyclerView.scrollToPosition(resultList.size() -1);
        }

        public void addData(List<PrivateChatModel> resultList){
            if(resultList != null && !resultList.isEmpty()){
                this.resultList.addAll(resultList);
                notifyDataSetChanged();
                mRecyclerView.scrollToPosition(resultList.size() -1);
            }

        }

        public void addData(PrivateChatModel model){
            if(model != null){
                this.resultList.add(model);
                notifyDataSetChanged();
                mRecyclerView.scrollToPosition(resultList.size() -1);
            }

        }


        @Override
        public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(PrivateChatActivity.this)
                    .inflate(R.layout.item_chat_layout,parent,false);

            return new ViewHolder(view);

    }

        @Override
        public void onBindViewHolder(ChatAdapter.ViewHolder holder, int position) {

            PrivateChatModel privateChatModel = resultList.get(position);
            String fromAccount = privateChatModel.getFromAccount();
            String fromImg = privateChatModel.getFromImg();
            String toImg = privateChatModel.getToImg();
            String toAccount = privateChatModel.getToAccount();
            String content = privateChatModel.getContent();
            if(account.equals(toAccount)){  // 接收到的

                holder.mToRightLayout.setVisibility(View.GONE);
                holder.mFromLeftLayout.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(fromImg)){
                    Glide.with(PrivateChatActivity.this).load(ImageUtils.buildImgUrl(fromImg)).into(holder.mFromLeftIv);
                }
                holder.mFromLeftTv.setText(content);

            }else if(account.equals(fromAccount)){   // 发出去的

                holder.mToRightLayout.setVisibility(View.VISIBLE);
                holder.mFromLeftLayout.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(fromImg)){
                    Glide.with(PrivateChatActivity.this).load(ImageUtils.buildImgUrl(fromImg)).into(holder.mToRightIv);
                }
                holder.mToRightTv.setText(content);
            }
        }

        @Override
        public int getItemCount() {
            if(resultList == null){
                return 0;
            }
            return resultList.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder{

            @BindView(R.id.chat_from_left_layout) RelativeLayout mFromLeftLayout;
            @BindView(R.id.chat_to_right_layout) RelativeLayout mToRightLayout;
            @BindView(R.id.from_img_iv) ImageView mFromLeftIv;
            @BindView(R.id.to_img_iv) ImageView mToRightIv;
            @BindView(R.id.to_right_tv) TextView mToRightTv;
            @BindView(R.id.from_left_tv) TextView mFromLeftTv;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }

    }


}
