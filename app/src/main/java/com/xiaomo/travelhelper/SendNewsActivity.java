package com.xiaomo.travelhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.xiaomo.travelhelper.http.HttpServiceFactory;
import com.xiaomo.travelhelper.http.ShareService;
import com.xiaomo.travelhelper.http.UploadService;
import com.xiaomo.travelhelper.model.BaseResult;
import com.xiaomo.travelhelper.model.UserConst;
import com.xiaomo.travelhelper.model.circle.UploadResult;
import com.xiaomo.travelhelper.util.SharedPreferencesUtil;
import com.xiaomo.travelhelper.view.img.GlideImageLoader;
import com.xiaomo.travelhelper.view.img.ImagePickerAdapter;
import com.xiaomo.travelhelper.view.img.SelectDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SendNewsActivity extends AppCompatActivity implements View.OnClickListener, ImagePickerAdapter.OnRecyclerViewItemClickListener{

    @BindView(R.id.send_news_back_iv) RelativeLayout mBackLayout;
    @BindView(R.id.send_news_content_et) EditText mContentEt;
    @BindView(R.id.send_news_btn) Button mSendBtn;


    private String account;
    private String token;
    private ShareService mShareService;

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 9;               //允许选择图片最大数


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_news);
        ButterKnife.bind(this);
        initListener();

        initImagePicker();
        initWidget();

        account = SharedPreferencesUtil.read(SendNewsActivity.this, UserConst.ACCOUNT,"");
        token = SharedPreferencesUtil.read(SendNewsActivity.this, UserConst.TOKEN,"");
        mShareService = HttpServiceFactory.buildShareService();
    }

    private void initListener(){
        mSendBtn.setOnClickListener(this);
        mBackLayout.setOnClickListener(this);
    }


    public static void actionStart(AppCompatActivity context){

        Intent intent = new Intent(context,SendNewsActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_news_back_iv:
                finish();
                break;
            case R.id.send_news_btn:
                mSendBtn.setClickable(false);
                mSendBtn.setText("发送中");
                mSendBtn.setTextColor(getResources().getColor(R.color.color_999999));
                String content = mContentEt.getText().toString();


                if(selImageList != null && !selImageList.isEmpty()){
                    // 上传图片处理
                    handleUpload(content);
                }else {
                    if(!TextUtils.isEmpty(content)){
                        send(content,null);
                    }else {
                        Toast.makeText(SendNewsActivity.this,"内容和图片不能均为空",Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    private void send(String content,String imgUrl){

        mShareService.send(account,token,content,imgUrl).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<BaseResult>() {
            @Override
            public void onCompleted() {
                mSendBtn.setClickable(true);
                mSendBtn.setText("发送");
                mSendBtn.setTextColor(getResources().getColor(R.color.color_ffffff));
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(SendNewsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                mSendBtn.setClickable(true);
                mSendBtn.setText("发送");
                mSendBtn.setTextColor(getResources().getColor(R.color.color_ffffff));
            }

            @Override
            public void onNext(BaseResult baseResult) {
                if(baseResult != null){
                    Toast.makeText(SendNewsActivity.this,baseResult.getMsg(),Toast.LENGTH_SHORT).show();
                    if(baseResult.isSuccess()){
                        finish();
                    }
                }else{
                    Toast.makeText(SendNewsActivity.this,"未知错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    private void initWidget() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.send_news_recyclerView);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style.transparentFrameWindowStyle,
                listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }


    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                List<String> names = new ArrayList<>();
                names.add("拍照");
                names.add("相册");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0: // 直接调起相机
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(SendNewsActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(SendNewsActivity.this, ImageGridActivity.class);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }

                    }
                }, names);


                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    ArrayList<ImageItem> images = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }
    }


    private void handleUpload(final String content){

        try{
            List<MultipartBody.Part> filePartList = new ArrayList<MultipartBody.Part>();
            for(ImageItem imageItem : selImageList){
                filePartList.add(createFilePart("file",imageItem.path, imageItem.mimeType));
            }
            UploadService uploadService = HttpServiceFactory.buildUploadService();
            uploadService.uploadFile(filePartList).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<UploadResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(SendNewsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            mSendBtn.setClickable(true);
                            mSendBtn.setText("发送");
                            mSendBtn.setTextColor(getResources().getColor(R.color.color_ffffff));
                        }

                        @Override
                        public void onNext(UploadResult uploadResult) {
                            if(uploadResult != null){
                                if(uploadResult.isSuccess()){
                                    List<String> stringList = uploadResult.getData();
                                    StringBuffer sb = new StringBuffer();
                                    for(String str : stringList){
                                        sb.append(str + ";");
                                    }
                                    send(content,sb.toString());
                                    return;
                                }else {
                                    Toast.makeText(SendNewsActivity.this,uploadResult.getMsg(),Toast.LENGTH_SHORT).show();
                                }
                            }
                            mSendBtn.setClickable(true);
                            mSendBtn.setText("发送");
                            mSendBtn.setTextColor(getResources().getColor(R.color.color_ffffff));
                        }
                    });
        }catch (Exception e){
            Toast.makeText(SendNewsActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }

    private MultipartBody.Part createFilePart(String key, String filePath,String mimeType){
        File file = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse(mimeType),file);
        return MultipartBody.Part.createFormData(key,
                filePath.substring(filePath.lastIndexOf("/")+1),requestBody);
    }




}
