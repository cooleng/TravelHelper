package com.xiaomo.travelhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.xiaomo.travelhelper.http.HttpServiceFactory;
import com.xiaomo.travelhelper.http.ShareService;
import com.xiaomo.travelhelper.model.BaseResult;
import com.xiaomo.travelhelper.model.UserConst;
import com.xiaomo.travelhelper.model.circle.ShareResult;
import com.xiaomo.travelhelper.util.ImageUtils;
import com.xiaomo.travelhelper.util.SharedPreferencesUtil;
import com.xiaomo.travelhelper.util.UIUtils;

import com.xiaomo.travelhelper.view.CollapsedTextView;
import com.xiaomo.travelhelper.view.nicelayout.ImageNice9Layout;
import com.xiaomo.travelhelper.view.nicelayout.picshower.ViewPagerActivity;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.imallan.jellyrefresh.JellyRefreshLayout;
import uk.co.imallan.jellyrefresh.PullToRefreshLayout;

/**
 * 朋友圈分享 activity 页面
 */
public class ShareNewsActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.share_news_iv) ImageView mShareIv;
    @BindView(R.id.share_news_back_iv) RelativeLayout mBackLayout;
    @BindView(R.id.share_news_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.share_refresh_layout) JellyRefreshLayout mRefreshLayout;

    private List<ShareResult.DataEntity> mShareResultList;
    private LinearLayoutManager linearLayoutManager;
    private MyAdapter mMyAdapter;
    private String account;
    private String token;
    private ShareService mShareService;
    private int currentPage = 1;
    private int pageSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_news);
        ButterKnife.bind(this);


        account = SharedPreferencesUtil.read(ShareNewsActivity.this, UserConst.ACCOUNT,"");
        token = SharedPreferencesUtil.read(ShareNewsActivity.this, UserConst.TOKEN,"");
        mShareService = HttpServiceFactory.buildShareService();

        initListener();
        initRecyclerView();
        initRefreshLayout();
        initData();

    }

    private void initListener(){

        mBackLayout.setOnClickListener(this);
        mShareIv.setOnClickListener(this);

    }

    private void initRefreshLayout(){
        TextView tv = new TextView(ShareNewsActivity.this);
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
                        initData();
                    }
                }, 500);

            }
        });
    }

    private void initData(){

            pageSize = 10;
            currentPage = 1;
            mShareService.listByPage(account,token,pageSize,currentPage).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ShareResult>() {
                        @Override
                        public void onCompleted() {
                            mRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(ShareNewsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            mRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onNext(ShareResult shareResult) {
                            if(shareResult != null && shareResult.isSuccess()) {
                                currentPage++;
                                mShareResultList = shareResult.getData();
                                mMyAdapter = new MyAdapter(mShareResultList);
                                mRecyclerView.setAdapter(mMyAdapter);

                            }else{
                                if(shareResult == null){
                                    Toast.makeText(ShareNewsActivity.this,"未知错误",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(ShareNewsActivity.this,shareResult.getMsg(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
    }


    private void initRecyclerView(){

        linearLayoutManager = new LinearLayoutManager(ShareNewsActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

    }

    private View getCommentPopupView(){
        return LayoutInflater.from(ShareNewsActivity.this).inflate(R.layout.comment_popup_layout,null);

    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        List<ShareResult.DataEntity> mShareResults;

        private int normalType = 0;     // 第一种ViewType，正常的item
        private int footType = 1;       // 第二种ViewType，底部的提示View

        public final String PULL_TO_LOAD = "PULL_TO_LOAD";
        public final String LOADING = "LOADING";
        public final String NOT_MORE = "NOT_MORE";
        public String status = PULL_TO_LOAD;

        public MyAdapter( List<ShareResult.DataEntity> shareResults) {
            this.mShareResults = shareResults;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // 根据返回的ViewType，绑定不同的布局文件，这里只有两种
            if (viewType == normalType) {
                View view = LayoutInflater.from(ShareNewsActivity.this)
                        .inflate(R.layout.item_share_news_layout,parent,false);
                return new ViewHolder(view);

            } else {
                View view = LayoutInflater.from(ShareNewsActivity.this)
                        .inflate(R.layout.item_load_more_layout,parent,false);
                return new FootHolder(view);
            }


        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

           if(holder instanceof ViewHolder){
               final ViewHolder viewHolder = (ViewHolder) holder;
               final ShareResult.DataEntity shareResult = mShareResults.get(position);
               String imgUrl = shareResult.getImgUrl();
               String img = shareResult.getImg();
               viewHolder.usernameTv.setText(shareResult.getUsername());
               viewHolder.shareTimeTv.setText(shareResult.getCreateTime());
               // 内容
               if(TextUtils.isEmpty(shareResult.getContent())){
                   viewHolder.shareContentCtv.setVisibility(View.GONE);
               }else {
                   viewHolder.shareContentCtv.setVisibility(View.VISIBLE);
                   viewHolder.shareContentCtv.setText( shareResult.getContent());
               }
               // 图片
               List<String> stringList = ImageUtils.buildImgUrlList(imgUrl);
               if(stringList == null || stringList.isEmpty()){
                   viewHolder.mImageNice9Layout.setVisibility(View.GONE);
               }else {
                   bindData(viewHolder.mImageNice9Layout,stringList);
               }
               final List<ShareResult.DataEntity.CommentListEntity> commentList = shareResult.getCommentList();
               if(commentList == null || commentList.isEmpty()){
                   viewHolder.mCommentRecyclerView.setVisibility(View.GONE);
               }else{
                   viewHolder.mCommentRecyclerView.setVisibility(View.VISIBLE);
                   LinearLayoutManager layoutManager = new LinearLayoutManager(ShareNewsActivity.this);
                   layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                   viewHolder.mCommentRecyclerView.setLayoutManager(layoutManager);
                   viewHolder.mCommentRecyclerView.setAdapter(new CommentAdapter(commentList));
               }

               if(!TextUtils.isEmpty(img)){
                   Glide.with(ShareNewsActivity.this).load(ImageUtils.buildImgUrl(img)).into(viewHolder.mImageView);
               }

               viewHolder.commentLayout.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       final View popupView = getCommentPopupView();
                       final PopupWindow popupWindow = new PopupWindow(popupView);
                       popupWindow.setWidth(QMUIDisplayHelper.getScreenWidth(ShareNewsActivity.this));
                       popupWindow.setHeight(UIUtils.dip2Px(ShareNewsActivity.this,56));
                       popupWindow.setAnimationStyle(R.style.typePopupWinStyle);
                       popupWindow.setOutsideTouchable(true);
                       popupWindow.setTouchable(true);
                       popupWindow.setFocusable(true);
                       popupWindow.showAtLocation(popupView, Gravity.BOTTOM,0,0);

                       popupView.findViewById(R.id.comment_send_btn).setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               String comment = ((EditText)popupView.findViewById(R.id.comment_et))
                                       .getText().toString();
                                if(!TextUtils.isEmpty(comment)){
                                    mShareService.comment(account,token,shareResult.getId(),account,comment).subscribeOn(Schedulers.newThread())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Subscriber<BaseResult>() {
                                                @Override
                                                public void onCompleted() {
                                                    popupWindow.dismiss();
                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    Toast.makeText(ShareNewsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                                    popupWindow.dismiss();
                                                }

                                                @Override
                                                public void onNext(BaseResult baseResult) {
                                                    if(baseResult == null){
                                                        Toast.makeText(ShareNewsActivity.this,"未知错误",Toast.LENGTH_SHORT).show();
                                                    }else {
                                                        Toast.makeText(ShareNewsActivity.this,baseResult.getMsg(),Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }else{
                                    popupWindow.dismiss();
                                }

                           }
                       });

                   }

               });
           }else{
               final FootHolder footHolder = (FootHolder) holder;
               footHolder.mLayout.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if( PULL_TO_LOAD.equals(status)){
                           status = LOADING;
                           // 加载数据
                           updateData();
                       }else if(NOT_MORE.equals(status)){
                           footHolder.mTextView.setText("我是有底线的");
                       }
                   }
               });

           }

        }

        @Override
        public int getItemCount() {
            if(mShareResults == null){
                return 0;
            }
            return mShareResults.size() + 1;
        }

        public int getRealLastPosition() {
            return mShareResults.size();
        }

        // 暴露接口，更新数据源
        public void updateData() {
            // 在原有的数据之上增加新数据
            status = LOADING;
            mShareService.listByPage(account,token,pageSize,currentPage).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ShareResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(ShareNewsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onNext(ShareResult shareResult) {
                            if(shareResult != null && shareResult.isSuccess()) {

                                List<ShareResult.DataEntity> dataEntityList = shareResult.getData();
                                if(dataEntityList.isEmpty()){
                                    status = NOT_MORE;
                                }else {
                                    currentPage++;
                                    mShareResults.addAll(dataEntityList);
                                    notifyDataSetChanged();
                                    status = PULL_TO_LOAD;
                                }
                            }else{
                                if(shareResult == null){
                                    Toast.makeText(ShareNewsActivity.this,"未知错误",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(ShareNewsActivity.this,shareResult.getMsg(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

        }


        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                return footType;
            } else {
                return normalType;
            }
        }

        class FootHolder extends RecyclerView.ViewHolder{

            @BindView(R.id.load_more_layout) LinearLayout mLayout;
            @BindView(R.id.load_more_tips) TextView mTextView;

            public FootHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }

            @BindView(R.id.item_share_comment_layout) RelativeLayout commentLayout;
            @BindView(R.id.item_share_content_ctv) CollapsedTextView shareContentCtv;
            @BindView(R.id.item_share_time_tv) TextView shareTimeTv;
            @BindView(R.id.item_share_image_nice9_layout) ImageNice9Layout mImageNice9Layout;
            @BindView(R.id.item_share_head_iv) ImageView mImageView;
            @BindView(R.id.item_share_username_tv) TextView usernameTv;
            @BindView(R.id.comment_recycler_view) RecyclerView mCommentRecyclerView;
        }

    }


    public static void actionStart(AppCompatActivity context){

        Intent intent = new Intent(context,ShareNewsActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share_news_back_iv:
                finish();
                break;
            case R.id.share_news_iv:
                SendNewsActivity.actionStart(ShareNewsActivity.this);
                break;

        }
    }



    public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

        private List<ShareResult.DataEntity.CommentListEntity> mList;

        public CommentAdapter(List<ShareResult.DataEntity.CommentListEntity> list) {
            mList = list;
        }

        @Override
        public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(ShareNewsActivity.this)
                    .inflate(R.layout.item_comment_layout,parent,false);
            return new ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {

            String username = mList.get(position).getUsername();
            String content = mList.get(position).getContent();
            holder.mCommentUsernameTv.setText(username + ": ");
            holder.mCommentConentTv.setText(content);

        }

        @Override
        public int getItemCount() {
            if(mList == null){
                return 0;
            }
            return mList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.item_comment_username_tv) TextView mCommentUsernameTv;
            @BindView(R.id.item_comment_content_tv) TextView mCommentConentTv;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }



    // 绑定图片数据
    private void bindData(ImageNice9Layout imageNice9Layout, final List<String> picStrings) {
        imageNice9Layout.setVisibility(View.VISIBLE);
        imageNice9Layout.bindData(picStrings);
        imageNice9Layout.setItemDelegate(new ImageNice9Layout.ItemDelegate() {
            @Override
            public void onItemClick(int position) {
                ViewPagerActivity.actionStart(ShareNewsActivity.this,(ArrayList<String>) picStrings);
            }
        });
    }


}
