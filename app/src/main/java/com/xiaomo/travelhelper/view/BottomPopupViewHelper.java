package com.xiaomo.travelhelper.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.xiaomo.travelhelper.R;
import com.xiaomo.travelhelper.util.UIUtils;


/**
 * 底部弹出框
 */

public class BottomPopupViewHelper {

    private PopupWindow mPopupWindow;

    public void show(Context context,View popupView,int heightDp){
        if(mPopupWindow == null){
            mPopupWindow = new PopupWindow(popupView);
            mPopupWindow.setWidth(QMUIDisplayHelper.getScreenWidth(context));
            mPopupWindow.setHeight(UIUtils.dip2Px(context,heightDp));
            mPopupWindow.setAnimationStyle(R.style.typePopupWinStyle);
        }
        if(!mPopupWindow.isShowing()){
            mPopupWindow.showAtLocation(popupView, Gravity.BOTTOM,0,0);
        }
    }

    public void dismiss(){
        if(mPopupWindow != null && mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }
    }
}
