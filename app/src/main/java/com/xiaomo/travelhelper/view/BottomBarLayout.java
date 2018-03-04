package com.xiaomo.travelhelper.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.xiaomo.travelhelper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 底部标签布局
 */
public class BottomBarLayout extends LinearLayout implements ViewPager.OnPageChangeListener {
    private static final String STATE_INSTANCE = "instance_state";
    private static final String STATE_ITEM = "state_item";
    private ViewPager mViewPager;
    private int mChildCount;
    private List<BottomBarItem> mItemViews;
    private int mCurrentItem;
    private boolean mSmoothScroll;
    private BottomBarLayout.OnItemSelectedListener onItemSelectedListener;

    public BottomBarLayout(Context context) {
        this(context, (AttributeSet)null);
    }

    public BottomBarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mItemViews = new ArrayList();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BottomBarLayout);
        this.mSmoothScroll = ta.getBoolean(R.styleable.BottomBarLayout_smoothScroll, true);
        ta.recycle();
    }

    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
    }

    public void setViewPager(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
        this.init();
    }

    private void init() {
        if(this.mViewPager == null) {
            throw new IllegalArgumentException("参数不能为空");
        } else {
            this.mChildCount = this.getChildCount();
            if(this.mViewPager.getAdapter().getCount() != this.mChildCount) {
                throw new IllegalArgumentException("LinearLayout的子View数量必须和ViewPager条目数量一致");
            } else {
                for(int i = 0; i < this.mChildCount; ++i) {
                    if(!(this.getChildAt(i) instanceof BottomBarItem)) {
                        throw new IllegalArgumentException("AlphaIndicator的子View必须是AlphaView");
                    }

                    BottomBarItem bottomBarItem = (BottomBarItem)this.getChildAt(i);
                    this.mItemViews.add(bottomBarItem);
                    bottomBarItem.setOnClickListener(new BottomBarLayout.MyOnClickListener(i));
                }

                ((BottomBarItem)this.mItemViews.get(this.mCurrentItem)).setStatus(true);
                this.mViewPager.setOnPageChangeListener(this);
            }
        }
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public void onPageSelected(int position) {
        this.resetState();
        ((BottomBarItem)this.mItemViews.get(position)).setStatus(true);
        if(this.onItemSelectedListener != null) {
            this.onItemSelectedListener.onItemSelected(this.getBottomItem(position), position);
        }

        this.mCurrentItem = position;
    }

    public void onPageScrollStateChanged(int state) {
    }

    private void resetState() {
        if(this.mCurrentItem < this.mItemViews.size()) {
            ((BottomBarItem)this.mItemViews.get(this.mCurrentItem)).setStatus(false);
        }

    }

    public void setCurrentItem(int currentItem) {
        this.mCurrentItem = currentItem;
        this.mViewPager.setCurrentItem(this.mCurrentItem, this.mSmoothScroll);
    }

    public void setUnread(int position, int unreadNum) {
        ((BottomBarItem)this.mItemViews.get(position)).setUnreadNum(unreadNum);
    }

    public void setMsg(int position, String msg) {
        ((BottomBarItem)this.mItemViews.get(position)).setMsg(msg);
    }

    public void hideMsg(int position) {
        ((BottomBarItem)this.mItemViews.get(position)).hideMsg();
    }

    public void showNotify(int position) {
        ((BottomBarItem)this.mItemViews.get(position)).showNotify();
    }

    public void hideNotify(int position) {
        ((BottomBarItem)this.mItemViews.get(position)).hideNotify();
    }

    public int getCurrentItem() {
        return this.mCurrentItem;
    }

    public void setSmoothScroll(boolean mSmoothScroll) {
        this.mSmoothScroll = mSmoothScroll;
    }

    public BottomBarItem getBottomItem(int position) {
        return (BottomBarItem)this.mItemViews.get(position);
    }

    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instance_state", super.onSaveInstanceState());
        bundle.putInt("state_item", this.mCurrentItem);
        return bundle;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            Bundle bundle = (Bundle)state;
            this.mCurrentItem = bundle.getInt("state_item");
            this.resetState();
            ((BottomBarItem)this.mItemViews.get(this.mCurrentItem)).setStatus(true);
            super.onRestoreInstanceState(bundle.getParcelable("instance_state"));
        } else {
            super.onRestoreInstanceState(state);
        }

    }

    public void setOnItemSelectedListener(BottomBarLayout.OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(BottomBarItem var1, int var2);
    }

    private class MyOnClickListener implements OnClickListener {
        private int currentIndex;

        public MyOnClickListener(int i) {
            this.currentIndex = i;
        }

        public void onClick(View v) {
            if(BottomBarLayout.this.onItemSelectedListener != null && this.currentIndex == BottomBarLayout.this.mCurrentItem) {
                BottomBarLayout.this.onItemSelectedListener.onItemSelected(BottomBarLayout.this.getBottomItem(this.currentIndex), this.currentIndex);
            }

            BottomBarLayout.this.mViewPager.setCurrentItem(this.currentIndex, BottomBarLayout.this.mSmoothScroll);
        }
    }
}
