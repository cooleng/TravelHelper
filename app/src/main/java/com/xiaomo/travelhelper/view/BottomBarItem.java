package com.xiaomo.travelhelper.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaomo.travelhelper.R;
import com.xiaomo.travelhelper.util.UIUtils;

import java.util.Locale;


/**
 * 底部标签项
 */
public class BottomBarItem extends LinearLayout {
    private Context mContext;
    private int mIconNormalResourceId;
    private int mIconSelectedResourceId;
    private String mText;
    private int mTextSize;
    private int mTextColorNormal;
    private int mTextColorSelected;
    private int mMarginTop;
    private boolean mOpenTouchBg;
    private Drawable mTouchDrawable;
    private int mIconWidth;
    private int mIconHeight;
    private int mItemPadding;
    private ImageView mImageView;
    private TextView mTvUnread;
    private TextView mTvNotify;
    private TextView mTvMsg;
    private TextView mTextView;
    private int mUnreadTextSize;
    private int mMsgTextSize;
    private int unreadNumThreshold;

    public BottomBarItem(Context context) {
        this(context, (AttributeSet)null);
    }

    public BottomBarItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBarItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mTextSize = 12;
        this.mTextColorNormal = -6710887;
        this.mTextColorSelected = -12140517;
        this.mMarginTop = 0;
        this.mOpenTouchBg = false;
        this.mUnreadTextSize = 10;
        this.mMsgTextSize = 6;
        this.unreadNumThreshold = 99;
        this.mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BottomBarItem);
        this.mIconNormalResourceId = ta.getResourceId(R.styleable.BottomBarItem_iconNormal, -1);
        this.mIconSelectedResourceId = ta.getResourceId(R.styleable.BottomBarItem_iconSelected, -1);
        this.mText = ta.getString(R.styleable.BottomBarItem_itemText);
        this.mTextSize = ta.getDimensionPixelSize(R.styleable.BottomBarItem_itemTextSize, UIUtils.sp2px(this.mContext, (float)this.mTextSize));
        this.mTextColorNormal = ta.getColor(R.styleable.BottomBarItem_textColorNormal, this.mTextColorNormal);
        this.mTextColorSelected = ta.getColor(R.styleable.BottomBarItem_textColorSelected, this.mTextColorSelected);
        this.mMarginTop = ta.getDimensionPixelSize(R.styleable.BottomBarItem_itemMarginTop, UIUtils.dip2Px(this.mContext, this.mMarginTop));
        this.mOpenTouchBg = ta.getBoolean(R.styleable.BottomBarItem_openTouchBg, this.mOpenTouchBg);
        this.mTouchDrawable = ta.getDrawable(R.styleable.BottomBarItem_touchDrawable);
        this.mIconWidth = ta.getDimensionPixelSize(R.styleable.BottomBarItem_iconWidth, 0);
        this.mIconHeight = ta.getDimensionPixelSize(R.styleable.BottomBarItem_iconHeight, 0);
        this.mItemPadding = ta.getDimensionPixelSize(R.styleable.BottomBarItem_itemPadding, 0);
        this.mUnreadTextSize = ta.getDimensionPixelSize(R.styleable.BottomBarItem_unreadTextSize, UIUtils.sp2px(this.mContext, (float)this.mUnreadTextSize));
        this.mMsgTextSize = ta.getDimensionPixelSize(R.styleable.BottomBarItem_msgTextSize, UIUtils.sp2px(this.mContext, (float)this.mMsgTextSize));
        this.unreadNumThreshold = ta.getInteger(R.styleable.BottomBarItem_unreadThreshold, 99);
        ta.recycle();
        this.checkValues();
        this.init();
    }

    private void checkValues() {
        if(this.mIconNormalResourceId == -1) {
            throw new IllegalStateException("您还没有设置默认状态下的图标，请指定iconNormal的图标");
        } else if(this.mIconSelectedResourceId == -1) {
            throw new IllegalStateException("您还没有设置选中状态下的图标，请指定iconSelected的图标");
        } else if(this.mOpenTouchBg && this.mTouchDrawable == null) {
            throw new IllegalStateException("开启了触摸效果，但是没有指定touchDrawable");
        }
    }

    private void init() {
        this.setOrientation(VERTICAL);
        this.setGravity(17);
        View view = View.inflate(this.mContext, R.layout.item_bottom_bar, (ViewGroup)null);
        if(this.mItemPadding != 0) {
            view.setPadding(this.mItemPadding, this.mItemPadding, this.mItemPadding, this.mItemPadding);
        }

        this.mImageView = (ImageView)view.findViewById(R.id.iv_icon);
        this.mTvUnread = (TextView)view.findViewById(R.id.tv_unred_num);
        this.mTvMsg = (TextView)view.findViewById(R.id.tv_msg);
        this.mTvNotify = (TextView)view.findViewById(R.id.tv_point);
        this.mTextView = (TextView)view.findViewById(R.id.tv_text);
        this.mImageView.setImageResource(this.mIconNormalResourceId);
        if(this.mIconWidth != 0 && this.mIconHeight != 0) {
            ViewGroup.LayoutParams imageLayoutParams = this.mImageView.getLayoutParams();
            imageLayoutParams.width = this.mIconWidth;
            imageLayoutParams.height = this.mIconHeight;
            this.mImageView.setLayoutParams(imageLayoutParams);
        }

        this.mTextView.setTextSize(0, (float)this.mTextSize);
        this.mTvUnread.setTextSize(0, (float)this.mUnreadTextSize);
        this.mTvMsg.setTextSize(0, (float)this.mMsgTextSize);
        this.mTextView.setTextColor(this.mTextColorNormal);
        this.mTextView.setText(this.mText);
        android.widget.LinearLayout.LayoutParams textLayoutParams = (android.widget.LinearLayout.LayoutParams)this.mTextView.getLayoutParams();
        textLayoutParams.topMargin = this.mMarginTop;
        this.mTextView.setLayoutParams(textLayoutParams);
        if(this.mOpenTouchBg) {
            this.setBackground(this.mTouchDrawable);
        }

        this.addView(view);
    }

    public ImageView getImageView() {
        return this.mImageView;
    }

    public TextView getTextView() {
        return this.mTextView;
    }

    public void setIconNormalResourceId(int mIconNormalResourceId) {
        this.mIconNormalResourceId = mIconNormalResourceId;
    }

    public void setIconSelectedResourceId(int mIconSelectedResourceId) {
        this.mIconSelectedResourceId = mIconSelectedResourceId;
    }

    public void setStatus(boolean isSelected) {
        this.mImageView.setImageDrawable(this.getResources().getDrawable(isSelected?this.mIconSelectedResourceId:this.mIconNormalResourceId));
        this.mTextView.setTextColor(isSelected?this.mTextColorSelected:this.mTextColorNormal);
    }

    private void setTvVisiable(TextView tv) {
        this.mTvUnread.setVisibility(GONE);
        this.mTvMsg.setVisibility(GONE);
        this.mTvNotify.setVisibility(GONE);
        tv.setVisibility(VISIBLE);
    }

    public int getUnreadNumThreshold() {
        return this.unreadNumThreshold;
    }

    public void setUnreadNumThreshold(int unreadNumThreshold) {
        this.unreadNumThreshold = unreadNumThreshold;
    }

    public void setUnreadNum(int unreadNum) {
        this.setTvVisiable(this.mTvUnread);
        if(unreadNum <= 0) {
            this.mTvUnread.setVisibility(GONE);
        } else if(unreadNum <= this.unreadNumThreshold) {
            this.mTvUnread.setText(String.valueOf(unreadNum));
        } else {
            this.mTvUnread.setText(String.format(Locale.CHINA, "%d+", new Object[]{Integer.valueOf(this.unreadNumThreshold)}));
        }

    }

    public void setMsg(String msg) {
        this.setTvVisiable(this.mTvMsg);
        this.mTvMsg.setText(msg);
    }

    public void hideMsg() {
        this.mTvMsg.setVisibility(GONE);
    }

    public void showNotify() {
        this.setTvVisiable(this.mTvNotify);
    }

    public void hideNotify() {
        this.mTvNotify.setVisibility(GONE);
    }
}
