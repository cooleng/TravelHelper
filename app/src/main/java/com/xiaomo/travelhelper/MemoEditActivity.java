package com.xiaomo.travelhelper;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.qmuiteam.qmui.util.QMUIDrawableHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.xiaomo.travelhelper.dao.MemoDao;
import com.xiaomo.travelhelper.model.memo.MemoConstants;
import com.xiaomo.travelhelper.model.memo.MemoEditData;
import com.xiaomo.travelhelper.model.memo.MemoListItemModel;
import com.xiaomo.travelhelper.util.DateTimeUtil;
import com.xiaomo.travelhelper.util.PermissionUtil;
import com.xiaomo.travelhelper.util.SDCardUtil;
import com.xiaomo.travelhelper.view.datepicker.CustomDatePicker;
import com.xiaomo.travelhelper.view.img.GlideImageLoader;
import com.xiaomo.travelhelper.view.richtext.RichTextEditor;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiaomo.travelhelper.SendNewsActivity.REQUEST_CODE_SELECT;

/**
 * 备忘录编辑 activity 页面
 */
public class MemoEditActivity extends AppCompatActivity {

    private long mRecordId;
    private int mRequestCode;
    private MemoListItemModel mModel;
    private MemoDao mMemoDao;
    private QMUIBottomSheet mQMUIBottomSheet;

    @BindView(R.id.memo_edit_back_iv) RelativeLayout mBackIv;
    @BindView(R.id.memo_share_iv) ImageView mShareIv;
    @BindView(R.id.memo_save_iv) ImageView mSaveIv;
    @BindView(R.id.memo_delete_iv) ImageView mDeleteIv;
    @BindView(R.id.memo_more_iv) ImageView mMoreIv;
    @BindView(R.id.memo_insert_iv) ImageView mInsertIv;
    @BindView(R.id.memo_time_tv) TextView mTimeTv;
    @BindView(R.id.memo_month_tv) TextView mMonthTv;
    @BindView(R.id.memo_rich_et) RichTextEditor mRichTextEditor;


    public static final int REQUEST_CODE_SELECT = 100;
    private int maxImgCount = 1;               //允许选择图片最大数



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);
        ButterKnife.bind(this);
        initDataFromStarter();
        initImagePicker();
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
                mRichTextEditor.clearAllLayout();
                mTimeTv.setText(mModel.getCreateTime());
                mMonthTv.setText(mModel.getCreateMonth());
                mRichTextEditor.showMemoEditData(mModel.getEditDataList());
            }
        }
    }

    /**
     * 跳转到 MemoEditActivity
     */
    public static void actionStart(AppCompatActivity context, int requestCode, long recordId){

        Intent intent = new Intent(context,MemoEditActivity.class);
        intent.putExtra(MemoConstants.KEY_RECORD_ID,recordId);
        intent.putExtra(MemoConstants.KEY_REQUEST_CODE,requestCode);
        context.startActivityForResult(intent,requestCode);
        context.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    private void saveDataAndBack(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<MemoEditData> editDataList = mRichTextEditor.buildMemoEditData();
                if(editDataList != null && editDataList.size() > 0){

                    if(mModel == null){
                        mModel = new MemoListItemModel();
                        mModel.setCreateTime(DateTimeUtil.getDayAndMinute());
                        mModel.setCreateMonth(DateTimeUtil.getYearAndMonth());
                    }else{
                        List<MemoEditData> savedDataList = mModel.getEditDataList();
                        if(savedDataList != null && savedDataList.size() > 0){
                            for(MemoEditData savedData : savedDataList){
                                savedData.delete();
                            }
                        }
                    }
                    mModel.setEditDataList(editDataList);
                    mMemoDao.save(mModel);
                    for(MemoEditData data : editDataList){
                        data.setModel(mModel);
                        data.save();
                    }
                }
            }
        }).start();
        setResult(RESULT_OK);
        finish();
    }

    private void saveData(){

        List<MemoEditData> editDataList = mRichTextEditor.buildMemoEditData();
        if(editDataList == null || editDataList.size() <= 0){
            Toast.makeText(MemoEditActivity.this,"正文为空，保存失败",Toast.LENGTH_SHORT).show();
        }else {

            if (mModel == null) {
                mModel = new MemoListItemModel();
                mModel.setCreateTime(DateTimeUtil.getDayAndMinute());
                mModel.setCreateMonth(DateTimeUtil.getYearAndMonth());
            }else{
                List<MemoEditData> savedDataList = mModel.getEditDataList();
                if(savedDataList != null && savedDataList.size() > 0){
                    for(MemoEditData savedData : savedDataList){
                        savedData.delete();
                    }
                }
            }
            mModel.setEditDataList(editDataList);
            mMemoDao.save(mModel);
            for (MemoEditData data : editDataList) {
                data.setModel(mModel);
                data.save();
            }
            Toast.makeText(MemoEditActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
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

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null && !images.isEmpty()) {
                      mRichTextEditor.insertImage(images.get(0).path);
                }
            }
        }
    }


    @OnClick(R.id.memo_save_iv)
    public void save(){
        // 保存数据
        saveData();
    }

    @OnClick(R.id.memo_edit_back_iv)
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
                if(mQMUIBottomSheet != null){
                    mQMUIBottomSheet.dismiss();
                    mQMUIBottomSheet = null;
                }
                alarm();
            }
        });


        QMUIGroupListView.newSection(MemoEditActivity.this)
                .addItemView(itemWithSwitch, null)
                .addTo(qmuiGroupListView);

        mQMUIBottomSheet = new QMUIBottomSheet.BottomListSheetBuilder(MemoEditActivity.this)
                .addHeaderView(qmuiGroupListView)
                .addItem("截图")
                .addItem("发送到桌面")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        if(position == 0){
                            captureView();
                        }
                    }
                })
                .build();

        mQMUIBottomSheet.show();

    }

    /**
     * TODO 闹钟提醒
     */
    private void alarm(){

        CustomDatePicker picker = new CustomDatePicker(MemoEditActivity.this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间

               /* AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent intent = new Intent(MemoEditActivity.this,AlarmManager.class);
                PendingIntent pi = PendingIntent.getBroadcast(MemoEditActivity.this,MemoConstants.REQUEST_ALARM
                        ,intent,0);
                alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + 1000*15,pi);*/
                Toast.makeText(MemoEditActivity.this,"你设定的提醒为 " + time,Toast.LENGTH_SHORT).show();
            }
        }, "2017-01-01 00:00", "2020-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        picker.showSpecificTime(true); // 显示时和分
        picker.setIsLoop(true); // 允许循环滚动
        picker.show(DateTimeUtil.getCurrentTime());

    }

    /**
     * 截图
     */
    private void captureView(){

        QMUIDialog.CustomDialogBuilder dialogBuilder = new QMUIDialog.CustomDialogBuilder(MemoEditActivity.this);
        dialogBuilder.setLayout(R.layout.drawablehelper_createfromview);
        final QMUIDialog dialog = dialogBuilder.setTitle("截图").create();
        ImageView displayImageView = dialog.findViewById(R.id.createFromViewDisplay);
        Bitmap createFromViewBitmap = QMUIDrawableHelper.createBitmapFromView(mRichTextEditor);
        displayImageView.setImageBitmap(createFromViewBitmap);

        displayImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MemoEditActivity.this,"截图已保存",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
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
                                ImagePicker.getInstance().setSelectLimit(maxImgCount);
                                Intent intent = new Intent(MemoEditActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);

                                break;
                            case TAG_GET_LOCAL:
                                // 启动图片选择器
                                ImagePicker.getInstance().setSelectLimit(maxImgCount);
                                Intent intent1 = new Intent(MemoEditActivity.this, ImageGridActivity.class);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
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
                        if(mModel!= null){
                            mModel.delete();
                            List<MemoEditData> dataList = mModel.getEditDataList();
                            for(MemoEditData data : dataList){
                                data.delete();
                            }
                        }
                        Toast.makeText(MemoEditActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .show();
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }


}
