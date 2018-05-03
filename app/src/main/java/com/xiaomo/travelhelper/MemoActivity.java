package com.xiaomo.travelhelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.xiaomo.travelhelper.dao.MemoDao;
import com.xiaomo.travelhelper.model.memo.MemoConstants;
import com.xiaomo.travelhelper.model.memo.MemoEditData;
import com.xiaomo.travelhelper.model.memo.MemoListItemModel;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.imallan.jellyrefresh.JellyRefreshLayout;
import uk.co.imallan.jellyrefresh.PullToRefreshLayout;

/**
 * 备忘录列表 activity 页面
 */

public class MemoActivity extends AppCompatActivity implements View.OnClickListener {


        @BindView(R.id.memo_fragment_recycler_view)
        RecyclerView mRecyclerView;
        @BindView(R.id.refresh_layout)
        JellyRefreshLayout mRefreshLayout;
        private Context mContext;
        private MemoDao mMemoDao;
        @BindView(R.id.memo_add_iv)
        ImageView mAddIv;
        @BindView(R.id.memo_back_iv) RelativeLayout mBackIV;


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activiti_memo_list);
            ButterKnife.bind(this);
            mContext = this;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
            mMemoDao = new MemoDao();
            mRecyclerView.setAdapter( new MemoListAdapter(getModels()));
            TextView tv = new TextView(mContext);
            tv.setText("努力加载中...");
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
                            updateData(getModels());
                            mRefreshLayout.setRefreshing(false);
                        }
                    }, 1000);

                }
            });

            mAddIv.setOnClickListener(this);
            mBackIV.setOnClickListener(this);

        }

    /**
     * 跳转到 MemoActivity
     */
    public static void actionStart(AppCompatActivity context){

        Intent intent = new Intent(context,MemoActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }


        class MemoListAdapter extends RecyclerView.Adapter<MemoListAdapter.ViewHolder>{

            private List<MemoListItemModel> mModelList;

            public MemoListAdapter(List<MemoListItemModel> modelList) {
                this.mModelList = modelList;
                Collections.sort(modelList);
            }

            @Override
            public int getItemCount() {
                return mModelList == null ? 0 : mModelList.size();
            }

            @Override
            public MemoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_memo_list,parent,false);
                MemoListAdapter.ViewHolder viewHolder = new MemoListAdapter.ViewHolder(view);
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(final MemoListAdapter.ViewHolder holder, final int position) {

                final MemoListItemModel  model = mModelList.get(position);
                String firstContent = model.getFirstContent();
                String firstImagePath = model.getFirstImagePath();
                if(!TextUtils.isEmpty(firstImagePath)){
                    holder.mImageLayout.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(firstImagePath).into(holder.mRecordImageView);
                }
                if(!TextUtils.isEmpty(firstContent)){
                    holder.mContentView.setText(firstContent);
                }else if(!TextUtils.isEmpty(firstImagePath)){
                    holder.mContentView.setText("[图片]");
                }
                holder.mDateView.setText(model.getCreateMonth());
                holder.mTimeView.setText(model.getCreateTime());
                if(TextUtils.isEmpty(model.getTitle())){
                    holder.mContentView.setText("[图片]");
                }else{
                    holder.mTitleView.setText(model.getTitle());
                }


                // 设置年月显示与隐藏
                if(position > 0){
                    if(model.compareTo(mModelList.get(position-1)) == 0){
                        holder.mDateView.setVisibility(View.GONE);
                        holder.mLayout.setPadding(0,0,0,0);
                    }
                }

                if(position == 0){
                    holder.mLayout.setPadding(0,0,0,0);
                }

                // 单击mBgLayout
                holder.mBgLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(holder.mDeleteLayout.getVisibility() == View.VISIBLE){
                            holder.mBgLayout.setBackgroundColor(getResources().getColor(R.color.white));
                            holder.mDeleteLayout.setVisibility(View.GONE);
                        }else {
                            // 跳转到详情页面
                            MemoEditActivity.actionStart((AppCompatActivity)mContext,
                                    MemoConstants.REQUEST_DETAIL,model.getId());
                        }
                    }
                });

                // 长按mBgLayout
                holder.mBgLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        if(holder.mDeleteLayout.getVisibility() != View.VISIBLE){
                            holder.mBgLayout.setBackgroundColor(getResources().getColor(R.color.tab_gb));
                            holder.mDeleteLayout.setVisibility(View.VISIBLE);
                        }
                        return true;
                    }
                });

                //单击mDeleteImageView
                holder.mDeleteImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 删除处理
                        new QMUIDialog.MessageDialogBuilder(mContext)
                                .setTitle("标题")
                                .setMessage("确定要删除吗？")
                                .addAction("取消", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                    }
                                })
                                .addAction("确定", new QMUIDialogAction.ActionListener() {
                                    @Override
                                    public void onClick(QMUIDialog dialog, int index) {
                                        dialog.dismiss();
                                        model.delete();
                                        List<MemoEditData> dataList = model.getEditDataList();
                                        for(MemoEditData data : dataList){
                                            data.delete();
                                        }
                                        updateData(mMemoDao.findAll());
                                        Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show();
                    }
                });

            }

            class ViewHolder extends RecyclerView.ViewHolder{

                @BindView(R.id.item_list_view)
                LinearLayout mLayout;
                @BindView(R.id.bg_layout) LinearLayout mBgLayout;
                @BindView(R.id.content_layout) LinearLayout mContentLayout;
                @BindView(R.id.memo_record_iv_layout)
                RelativeLayout mImageLayout;
                @BindView(R.id.delete_layout)RelativeLayout mDeleteLayout;
                @BindView(R.id.date_tv) TextView mDateView;
                @BindView(R.id.title_tv)TextView mTitleView;
                @BindView(R.id.content_tv)TextView mContentView;
                @BindView(R.id.time_tv)TextView mTimeView;
                @BindView(R.id.delete_image_view)ImageView mDeleteImageView;
                @BindView(R.id.memo_record_iv)ImageView mRecordImageView;

                public ViewHolder(View itemView) {
                    super(itemView);
                    ButterKnife.bind(this,itemView);
                }
            }

        }


        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            updateData(getModels());
        }






        public void updateData(List<MemoListItemModel> modelList){
            mRecyclerView.setAdapter(new MemoListAdapter(modelList));
        }

        private List<MemoListItemModel> getModels(){
            List<MemoListItemModel> models = mMemoDao.findAll();
            if(models.size() == 0){
                Toast.makeText(mContext,"嗯，你还没有写过日记，加油！！",Toast.LENGTH_LONG).show();
            }
            return models;
        }

        private List<MemoListItemModel> getModels(String query){

            if(TextUtils.isEmpty(query)){
                return mMemoDao.findAll();
            }
            return mMemoDao.findByLikeTitle("%" + query + "%");
        }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.memo_add_iv:
                MemoEditActivity.actionStart(this,MemoConstants.REQUEST_ADD,0);
                break;
            case R.id.memo_back_iv:
                finish();
                break;

        }
    }
}
