package com.xiaomo.travelhelper.view.nicelayout;

import android.content.Context;
import android.support.annotation.NonNull;

import com.alibaba.android.vlayout.VirtualLayoutManager;

/**
 * 禁止 RecyclerView 滚动
 */

public class CustomVirtualLayoutManager extends VirtualLayoutManager {

    private boolean isScrollEnabled = true;

    public CustomVirtualLayoutManager(@NonNull Context context) {
        super(context);
    }

    public CustomVirtualLayoutManager(@NonNull Context context, int orientation) {
        super(context, orientation);
    }

    public CustomVirtualLayoutManager(@NonNull Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    /**
     * 禁止 RecyclerView 滚动
     * @return false
     */
    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }

    public void setScrollEnabled(boolean isScrollEnabled){
        this.isScrollEnabled = isScrollEnabled;
    }

}
