package com.xiaomo.travelhelper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.xiaomo.travelhelper.dao.MemoDao;
import com.xiaomo.travelhelper.model.memo.MemoConstants;
import com.xiaomo.travelhelper.model.memo.MemoEditData;
import com.xiaomo.travelhelper.model.memo.MemoListItemModel;
import com.xiaomo.travelhelper.util.PermissionUtil;
import com.xiaomo.travelhelper.util.SDCardUtil;
import com.xiaomo.travelhelper.util.UIUtils;
import com.xiaomo.travelhelper.view.richtext.DeletableEditText;
import com.xiaomo.travelhelper.view.richtext.RichTextEditor;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MemoEditActivity extends AppCompatActivity {

    private long mRecordId;
    private int mRequestCode;
    private Uri mPhotoUri;
    private MemoListItemModel mModel;
    private MemoDao mMemoDao;

    @BindView(R.id.memo_back_iv) ImageView mBackIv;
    @BindView(R.id.memo_share_iv) ImageView mShareIv;
    @BindView(R.id.memo_save_iv) ImageView mSaveIv;
    @BindView(R.id.memo_delete_iv) ImageView mDeleteIv;
    @BindView(R.id.memo_more_iv) ImageView mMoreIv;
    @BindView(R.id.memo_insert_iv) ImageView mInsertIv;
    @BindView(R.id.memo_time_tv) TextView mTimeTv;
    @BindView(R.id.memo_month_tv) TextView mMonthTv;
    @BindView(R.id.memo_rich_et) RichTextEditor mRichTextEditor;
    @BindView(R.id.memo_title_et) DeletableEditText mTitleEt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);
        ButterKnife.bind(this);
        initDataFromStarter();
    }

    /**
     * 获取启动传递的数据
     */
    public void initDataFromStarter(){
        mMemoDao = new MemoDao();
        Intent intent = getIntent();
        if(intent != null){
            mRecordId = intent.getLongExtra(MemoConstants.KEY_RECORD_ID,0);
            mRequestCode = intent.getIntExtra(MemoConstants.KEY_REQUEST_CODE,0);
        }

        if(mRequestCode == MemoConstants.REQUEST_DETAIL){
            mModel = mMemoDao.findById(mRecordId);
            if(mModel != null){
                mTimeTv.setText(mModel.getCreateTime());
                mMonthTv.setText(mModel.getCreateMonth());
                mTitleEt.setText(mModel.getTitle());
                List<MemoEditData> editDataList = mModel.getEditDataList();
                if(editDataList != null){
                    for(MemoEditData data : editDataList){
                        if(!TextUtils.isEmpty(data.getInputStr())){
                            mRichTextEditor.addEditTextAtIndex(mRichTextEditor.getLastIndex()+1,data.getInputStr());
                        }
                        if(!TextUtils.isEmpty(data.getImagePath())){
                            String path = data.getImagePath();
                            mRichTextEditor.insertImage(BitmapFactory.decodeFile(path),path);
                        }
                    }
                }
            }
        }
    }


    @OnClick(R.id.memo_save_iv)
    public void save(){
        // 保存数据
        saveData();
    }

    @OnClick(R.id.memo_back_iv)
    public void saveAndBack(){
        // 保存数据并返回
        saveDataAndBack();
    }

    @OnClick(R.id.memo_share_iv)
    public void shareRecord(){
        Toast.makeText(this,"分享记录",Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.memo_insert_iv)
    public void insertPic(){
        showSimpleBottomSheetGrid();
    }

    @OnClick(R.id.memo_delete_iv)
    public void deleteRecord(){
        showMessagePositiveDialog();
    }

    @OnClick(R.id.memo_more_iv)
    public void moreOptions(){
        showSimpleBottomSheetList();
    }

    /**
     * 从底部展现更多操作列表
     */
    private void showSimpleBottomSheetList() {

        QMUIGroupListView qmuiGroupListView = new QMUIGroupListView(MemoEditActivity.this);
        QMUICommonListItemView itemWithSwitch = qmuiGroupListView.createItemView("提醒");
        itemWithSwitch.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        itemWithSwitch.getSwitch().setChecked(false);
        itemWithSwitch.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(MemoEditActivity.this, "checked = " + isChecked, Toast.LENGTH_SHORT).show();
            }
        });


        QMUIGroupListView.newSection(MemoEditActivity.this)
                .addItemView(itemWithSwitch, null)
                .addTo(qmuiGroupListView);

        new QMUIBottomSheet.BottomListSheetBuilder(MemoEditActivity.this)
                .addHeaderView(qmuiGroupListView)
                .addItem("截图")
                .addItem("发送到桌面")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        Toast.makeText(MemoEditActivity.this, "Item " + (position + 1), Toast.LENGTH_SHORT).show();
                    }
                })
                .build()
                .show();
    }

    private void showSimpleBottomSheetGrid() {

        final int TAG_GET_CAMERA = 1;
        final int TAG_GET_LOCAL = 2;
        QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet
                .BottomGridSheetBuilder(MemoEditActivity.this);
        builder.addItem(R.mipmap.local_icon, "本地获取", TAG_GET_LOCAL, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.camera_icon, "拍照获取", TAG_GET_CAMERA, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView) {
                        dialog.dismiss();
                        int tag = (int) itemView.getTag();
                        switch (tag) {
                            case TAG_GET_CAMERA:
                                // 启动相机
                                if(!PermissionUtil.isPermissions(MemoEditActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                                        ,Manifest.permission.CAMERA})){

                                    PermissionUtil.requestPermissionsCameraImg(MemoEditActivity.this
                                            ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                                                    ,Manifest.permission.WRITE_EXTERNAL_STORAGE
                                                    ,Manifest.permission.CAMERA});
                                }else {
                                    mPhotoUri = SDCardUtil.startCamera(MemoEditActivity.this,SDCardUtil.getSystemCameraPath());
                                }

                                break;
                            case TAG_GET_LOCAL:

                                if(!PermissionUtil.isPermissions(MemoEditActivity.this
                                        ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                                                ,Manifest.permission.WRITE_EXTERNAL_STORAGE})){

                                    PermissionUtil.requestPermissionsLocalImg(MemoEditActivity.this
                                            ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                                                    ,Manifest.permission.WRITE_EXTERNAL_STORAGE});
                                }else{
                                    SDCardUtil.startImageMedia(MemoEditActivity.this);
                                }
                                break;
                        }
                    }
                }).build().show();
    }

    private void showMessagePositiveDialog() {
        new QMUIDialog.MessageDialogBuilder(MemoEditActivity.this)
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
                        Toast.makeText(MemoEditActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }


    /**
     * 跳转到 MemoEditActivity
     */
    public static void actionStart(Context context,Fragment fragment, int requestCode, long recordId){

        Intent intent = new Intent(context,MemoEditActivity.class);
        intent.putExtra(MemoConstants.KEY_RECORD_ID,recordId);
        intent.putExtra(MemoConstants.KEY_REQUEST_CODE,requestCode);
        fragment.startActivityForResult(intent,requestCode);
        ((AppCompatActivity)context).overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    private void saveDataAndBack(){

        List<MemoEditData> editDataList = mRichTextEditor.buildMemoEditData();
        String title = mTitleEt.getText().toString();
        if(!TextUtils.isEmpty(title) && editDataList != null && editDataList.size() > 0){

            if(mModel == null){
                mModel = new MemoListItemModel();
            }
            mModel.setEditDataList(editDataList);
            mModel.setTitle(title);
            mModel.setCreateTime("1日 15:35");
            mModel.setCreateMonth("2018年3月");
            mMemoDao.save(mModel);
        }
        setResult(RESULT_OK);
        finish();
    }

    private void saveData(){

        List<MemoEditData> editDataList = mRichTextEditor.buildMemoEditData();
        String title = mTitleEt.getText().toString();
        if(TextUtils.isEmpty(title) || editDataList == null || editDataList.size() <= 0){
            Toast.makeText(MemoEditActivity.this,"标题或者正文为空，保存失败",Toast.LENGTH_SHORT).show();
        }else{

            if(mModel == null){
                mModel = new MemoListItemModel();
            }
            mModel.setEditDataList(editDataList);
            mModel.setTitle(title);
            mModel.setCreateTime("1日 15:35");
            mModel.setCreateMonth("2018年3月");
            mMemoDao.save(mModel);
            Toast.makeText(MemoEditActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onBackPressed() {
        // 保存数据并返回
        saveDataAndBack();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == MemoConstants.REQUEST_TAKE_PHOTO) {
           handleTakePhotoCallback(data);
        }else if(requestCode == MemoConstants.REQUEST_TAKE_LOCAL){
            handleTakeLocalCallback(data);
        }

    }

    /**
     * 处理拍照回调
     * @param data
     */
    private void handleTakePhotoCallback(Intent data){

        Uri uri = null;
        if (data != null && data.getData() != null) {
            uri = data.getData();
        }
        if (uri == null) {
            if (mPhotoUri != null) {
                uri = mPhotoUri;
            }
        }
        handleImageUri(uri);
    }



    /**
     * 处理本地相册回调
     * @param data
     */
    private void handleTakeLocalCallback(Intent data){

        Uri uri = data.getData();
        if(uri != null){
            handleImageUri(uri);
        }
    }

    // 压缩处理图片后复制到 App 指定默认目录
    private void handleImageUri(Uri uri) {

        String path = SDCardUtil.getFilePathByUri(MemoEditActivity.this,uri);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        mRichTextEditor.insertImage(bitmap,path);

       /* UIUtils.compressBitmap(MemoEditActivity.this
                , new File(SDCardUtil.getFilePathByUri(MemoEditActivity.this,uri)), SDCardUtil.getPictureDir()
                , new UIUtils.CompressListener() {
                    @Override
                    public void onStart() {
                        Log.i("compressBitmap-onStart","start");
                    }

                    @Override
                    public void onSuccess(Object o,String path) {
                        // 压缩成功插入到富文本
                        Log.i("compressBitmap-success",path);
                        Bitmap bitmap = (Bitmap) o;
                        mRichTextEditor.insertImage(bitmap,path);

                    }

                    @Override
                    public void onFail(String error) {
                        Log.i("compressBitmap-onFail",error);
                    }
       });*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PermissionUtil.PERMISSIONS_REQUEST_CAMERA_IMG: {
                if(PermissionUtil.checkRequestResult(grantResults)){
                    SDCardUtil.startCamera(MemoEditActivity.this,SDCardUtil.getSystemCameraPath());
                }else {
                    Toast.makeText(MemoEditActivity.this,"无权限",Toast.LENGTH_SHORT);
                }
                return;
            }
            case PermissionUtil.PERMISSIONS_REQUEST_LOCAL_IMG:{
                if(PermissionUtil.checkRequestResult(grantResults)){
                    SDCardUtil.startImageMedia(MemoEditActivity.this);
                }else {
                    Toast.makeText(MemoEditActivity.this,"无权限",Toast.LENGTH_SHORT);
                }
                return;
            }
        }
    }
}
