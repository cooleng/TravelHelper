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
import android.util.Log;
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
import com.xiaomo.travelhelper.model.memo.MemoListItemModel;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 备忘录 Fragment 页面
 */

public class MemoFragment extends Fragment {

    // fragment 中的 rootView
    private View mView;

    // 检索添加栏
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private Fragment mFragment;
    private MemoDao mMemoDao;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        mFragment = this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mMemoDao = new MemoDao();
        mView = inflater.inflate(R.layout.fragment_memo_layout,container,false);
        mToolbar = mView.findViewById(R.id.toolbar);
        mRecyclerView = mView.findViewById(R.id.memo_fragment_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(new MemoListAdapter(getModels()));
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        // 给搜索添加栏左边置空
        mToolbar.setTitle("");
        setHasOptionsMenu(true);
        return mView;

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.search_view_menu, menu);

        //找到searchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 提交查询文本
                updateData(getModels(query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 改变查询文本
                updateData(getModels(newText));
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_add:
                // 跳转到添加页
                MemoEditActivity.actionStart(mContext,mFragment,MemoConstants.REQUEST_ADD,0);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
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
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_memo_list,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

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
            holder.mTitleView.setText(model.getTitle());

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
                        MemoEditActivity.actionStart(mContext,mFragment,
                                MemoConstants.REQUEST_DETAIL,model.getId());
                    }
                }
            });

            // 长按mBgLayout
            holder.mBgLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    if(holder.mDeleteLayout.getVisibility() != View.VISIBLE){
                        holder.mBgLayout.setBackgroundColor(getResources().getColor(R.color.greyWithAlpha));
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
                                    updateData(mMemoDao.findAll());
                                    Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                }
            });

        }

        class ViewHolder extends RecyclerView.ViewHolder{

            @BindView(R.id.item_list_view) LinearLayout mLayout;
            @BindView(R.id.bg_layout) LinearLayout mBgLayout;
            @BindView(R.id.content_layout) LinearLayout mContentLayout;
            @BindView(R.id.memo_record_iv_layout) RelativeLayout mImageLayout;
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

}
