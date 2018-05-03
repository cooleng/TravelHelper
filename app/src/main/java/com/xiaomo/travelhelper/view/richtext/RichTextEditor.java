package com.xiaomo.travelhelper.view.richtext;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;


import com.bumptech.glide.Glide;
import com.xiaomo.travelhelper.R;
import com.xiaomo.travelhelper.model.memo.MemoConstants;
import com.xiaomo.travelhelper.model.memo.MemoEditData;
import com.xiaomo.travelhelper.util.ImageUtils;
import com.xiaomo.travelhelper.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 可编辑富文本
 */
public class RichTextEditor extends ScrollView {
    private static final int EDIT_PADDING = 10; // edittext常规padding是10dp

    private Context mContext;
    private int viewTagIndex = 1; // 新生的view都会打一个tag，对每个view来说，这个tag是唯一的。
    private LinearLayout allLayout; // 这个是所有子view的容器，scrollView内部的唯一一个ViewGroup
    private LayoutInflater inflater;
    private OnKeyListener keyListener; // 所有EditText的软键盘监听器
    private OnClickListener btnListener; // 图片右上角红叉按钮监听器
    private OnFocusChangeListener focusListener; // 所有EditText的焦点监听listener
    private EditText lastFocusEdit; // 最近被聚焦的EditText
    private LayoutTransition mTransitioner; // 只在图片View添加或remove时，触发transition动画
    private int editNormalPadding = 0; //

    public RichTextEditor(Context context) {
        this(context, null);
    }

    public RichTextEditor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichTextEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        // 初始化监听器
        intiListener();

        // 初始化allLayout
        inflater = LayoutInflater.from(context);
        allLayout = new LinearLayout(context);
        allLayout.setOrientation(LinearLayout.VERTICAL);
        setupLayoutTransitions();
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        //设置间距，防止生成图片时文字太靠边，不能用margin，否则有黑边
        allLayout.setPadding(50, 15, 50, 15);
        addView(allLayout, layoutParams);

        // 添加第一个默认输入框
        LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        EditText firstEdit = createEditText("请输入内容", UIUtils.dip2Px(context, EDIT_PADDING));
        allLayout.addView(firstEdit, firstEditParam);
        lastFocusEdit = firstEdit;

    }


    /**
     * 清除布局内容
     */
    public void clearAllLayout() {
        viewTagIndex = 1;
        lastFocusEdit = null;
        allLayout.removeAllViews();

        // 添加第一个默认输入框
        LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        EditText firstEdit = createEditText("", UIUtils.dip2Px(mContext, EDIT_PADDING));
        allLayout.addView(firstEdit, firstEditParam);
        lastFocusEdit = firstEdit;
    }

    /**
     * 获取最后一个子view位置
     * 0 开始 ， -1 表示没有子view
     * @return
     */
    public int getLastIndex() {
        int lastEditIndex = allLayout.getChildCount();
        return lastEditIndex -1;
    }


    /**
     * 根据焦点位置插入一张图片
     *
     * @param imagePath
     */
    public void insertImage(String imagePath) {
        String lastEditStr = lastFocusEdit.getText().toString();
        int cursorIndex = lastFocusEdit.getSelectionStart();
        String editStr1 = lastEditStr.substring(0, cursorIndex).trim();
        int lastEditIndex = allLayout.indexOfChild(lastFocusEdit);

        if (lastEditStr.length() == 0 || editStr1.length() == 0) {
            // 如果EditText为空，或者光标已经顶在了editText的最前面，则直接插入图片，并且EditText下移即可
            addImageViewAtIndex(lastEditIndex, imagePath);
        } else {
            // 如果EditText非空且光标不在最顶端，则需要添加新的imageView和EditText
            lastFocusEdit.setText(editStr1);
            String editStr2 = lastEditStr.substring(cursorIndex).trim();
            if (allLayout.getChildCount() - 1 == lastEditIndex) {
                addEditTextAtIndex(lastEditIndex + 1, editStr2);
            }

            addImageViewAtIndex(lastEditIndex + 1, imagePath);
            lastFocusEdit.requestFocus();
            lastFocusEdit.setSelection(lastFocusEdit.getText().toString().length());
        }
        hideKeyBoard();
    }


    /**
     * 在特定位置插入EditText
     *
     */
    public void addEditTextAtIndex(final int index, CharSequence editStr) {
        EditText editText2 = createEditText("", EDIT_PADDING);
        editText2.setText(editStr);
        editText2.setOnFocusChangeListener(focusListener);
        editText2.requestFocus();
        allLayout.addView(editText2, index);
    }

    /**
     * 在特定位置添加ImageView
     */
    public void addImageViewAtIndex(final int index, String imagePath) {
        final RelativeLayout imageLayout = createImageLayout();
        DataImageView imageView = imageLayout.findViewById(R.id.edit_imageView);
        Glide.with(getContext()).load(imagePath).into(imageView);
        imageView.setAbsolutePath(imagePath);//保留这句，后面保存数据会用
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//裁剪剧中

        allLayout.addView(imageLayout, index);
    }



    /**
     * 对外提供的接口, 生成编辑数据
     */
    public List<MemoEditData> buildMemoEditData() {
        List<MemoEditData> dataList = new ArrayList<MemoEditData>();
        int num = allLayout.getChildCount();
        for (int index = 0; index < num; index++) {
            View itemView = allLayout.getChildAt(index);
            MemoEditData itemData = new MemoEditData();
            if (itemView instanceof EditText) {
                EditText item = (EditText) itemView;
                if(!TextUtils.isEmpty(item.getText().toString())){
                    itemData.setKey(MemoConstants.KEY_TEXT);
                    itemData.setVal(item.getText().toString());
                    dataList.add(itemData);
                }

            } else if (itemView instanceof RelativeLayout) {
                DataImageView item = itemView.findViewById(R.id.edit_imageView);
                if(!TextUtils.isEmpty(item.getAbsolutePath())){
                    itemData.setKey(MemoConstants.KEY_IMAGE);
                    itemData.setVal(item.getAbsolutePath());
                    dataList.add(itemData);
                }
            }
        }

        return dataList;
    }

    /**
     * 对外接口，渲染编辑数据
     * @param memoEditDataList
     */
    public void showMemoEditData(List<MemoEditData> memoEditDataList){

        clearAllLayout();
        if(memoEditDataList == null){
            return;
        }
        for(int i = memoEditDataList.size()-1; i>=0;i--){
            MemoEditData data = memoEditDataList.get(i);
            if(MemoConstants.KEY_TEXT.equals(data.getKey())){
                // 添加一个 EditText
               addEditTextAtIndex(0,data.getVal());
            }else if(MemoConstants.KEY_IMAGE.equals(data.getKey())){
                // 添加一个 imageView
                addImageViewAtIndex(0,data.getVal());
            }
        }
        View view = allLayout.getChildAt(getLastIndex());
        if(view != null){
            view.requestFocus();
        }
    }


    /**
     * 生成文本输入框
     */
    private EditText createEditText(String hint, int paddingTop) {
        EditText editText = (EditText) inflater.inflate(R.layout.rich_edittext, null);
        editText.setOnKeyListener(keyListener);
        editText.setTag(viewTagIndex++);
        editText.setPadding(editNormalPadding, paddingTop, editNormalPadding, paddingTop);
        editText.setHint(hint);
        editText.setOnFocusChangeListener(focusListener);
        return editText;
    }


    /**
     * 隐藏小键盘
     */
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(lastFocusEdit.getWindowToken(), 0);
    }

    /**
     * 生成图片View
     */
    private RelativeLayout createImageLayout() {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(
                R.layout.edit_imageview, null);
        layout.setTag(viewTagIndex++);
        final View closeView = layout.findViewById(R.id.image_close);
        closeView.setTag(layout.getTag());
        closeView.setOnClickListener(btnListener);

        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(closeView.getVisibility() == GONE){
                    closeView.setVisibility(VISIBLE);
                }else if(closeView.getVisibility() == VISIBLE){
                    closeView.setVisibility(GONE);
                }
            }
        });

        return layout;
    }


    /**
     * 初始化监听器
     */
    private void intiListener(){

        //初始化键盘退格监听,主要用来处理点击回删按钮时，view的一些列合并操作
        keyListener = new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    EditText edit = (EditText) v;
                    onBackspacePress(edit);
                }
                return false;
            }
        };

        // 图片叉掉处理
        btnListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                RelativeLayout parentView = (RelativeLayout) v.getParent();
                onImageCloseClick(parentView);
            }
        };

        focusListener = new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    lastFocusEdit = (EditText) v;
                }
            }
        };

    }

    /**
     * 初始化transition动画
     */
    private void setupLayoutTransitions() {
        mTransitioner = new LayoutTransition();
        allLayout.setLayoutTransition(mTransitioner);
        mTransitioner.addTransitionListener(new LayoutTransition.TransitionListener() {

            @Override
            public void startTransition(LayoutTransition transition,
                                        ViewGroup container, View view, int transitionType) {

            }

            @Override
            public void endTransition(LayoutTransition transition,
                                      ViewGroup container, View view, int transitionType) {
                if (!transition.isRunning()
                        && transitionType == LayoutTransition.CHANGE_DISAPPEARING) {
                }
            }
        });
        mTransitioner.setDuration(300);
    }

    /**
     * 处理软键盘backSpace回退事件
     *
     * @param editTxt 光标所在的文本输入框
     */
    private void onBackspacePress(EditText editTxt) {
        int startSelection = editTxt.getSelectionStart();
        // 只有在光标已经顶到文本输入框的最前方，在判定是否删除之前的图片，或两个View合并
        if (startSelection == 0) {
            int editIndex = allLayout.indexOfChild(editTxt);
            View preView = allLayout.getChildAt(editIndex - 1); // 如果editIndex-1<0,
            // 则返回的是null
            if (null != preView) {
                if (preView instanceof RelativeLayout) {
                    // 光标EditText的上一个view对应的是图片
                    onImageCloseClick(preView);
                } else if (preView instanceof EditText) {
                    // 光标EditText的上一个view对应的还是文本框EditText
                    String str1 = editTxt.getText().toString();
                    EditText preEdit = (EditText) preView;
                    String str2 = preEdit.getText().toString();

                    allLayout.removeView(editTxt);

                    // 文本合并
                    preEdit.setText(str2 + str1);
                    preEdit.requestFocus();
                    preEdit.setSelection(str2.length(), str2.length());
                    lastFocusEdit = preEdit;
                }
            }
        }
    }

    /**
     * 处理图片叉掉的点击事件
     *
     * @param view 整个image对应的relativeLayout view
     */
    private void onImageCloseClick(View view) {
        allLayout.removeView(view);
    }



}
