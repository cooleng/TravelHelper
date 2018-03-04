package com.xiaomo.travelhelper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xiaomo.travelhelper.R;

/**
 * 顶部标签布局
 */

public class TopBarLayout extends LinearLayout {

    private Context mContext;
    private View mView;
    private ImageView mHeadImageView;
    private ImageView mMoreView;
    private ImageView mUpdateView;

    public TopBarLayout(Context context) {
        this(context, (AttributeSet)null);
    }

    public TopBarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;
        initView();

    }

    private void initView(){
        mView = View.inflate(this.mContext, R.layout.layout_top_bar, (ViewGroup)null);
        this.mHeadImageView = (ImageView) mView.findViewById(R.id.top_bar_head_image);
        this.mUpdateView = (ImageView) mView.findViewById(R.id.top_bar_update_view);
        this.mMoreView = (ImageView) mView.findViewById(R.id.top_bar_more_view);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        mView.setLayoutParams(layoutParams);
        this.addView(mView);

    }


    public ImageView getHeadImageView() {
        return mHeadImageView;
    }

    public ImageView getMoreView() {
        return mMoreView;
    }

    public ImageView getUpdateView() {
        return mUpdateView;
    }
}
