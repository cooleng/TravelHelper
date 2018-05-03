package com.xiaomo.travelhelper.model.memo;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 备忘录 List Item 数据模型
 */

public class MemoListItemModel extends DataSupport implements Comparable{

    private String createMonth;
    private String createTime;
    private String title;
    private String firstContent;
    private String firstImagePath;
    private List<MemoEditData> mEditDataList = new ArrayList<MemoEditData>();

    @Override
    public int compareTo(@NonNull Object o) {
        if(TextUtils.isEmpty(this.createMonth)){
            return -1;
        }
        if(o == null || ! (o instanceof MemoListItemModel)){
            return 1;
        }
        MemoListItemModel model = (MemoListItemModel) o;
        String month = model.getCreateMonth();
        if(TextUtils.isEmpty(month)){
            return  1;
        }

        return this.createMonth.compareTo(month);
    }

    public String getCreateMonth() {
        return createMonth;
    }

    public void setCreateMonth(String createMonth) {
        this.createMonth = createMonth;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstContent() {
        return firstContent;
    }

    public void setFirstContent(String firstContent) {
        this.firstContent = firstContent;
    }

    public long getId(){
        return super.getBaseObjId();
    }

    public List<MemoEditData> getEditDataList() {
        return mEditDataList;
    }

    public void setEditDataList(List<MemoEditData> editDataList) {
        boolean titleFlag = false;
        boolean contentFlag = false;
        boolean imageFlag = false;
        if(editDataList != null){
            for(MemoEditData data : editDataList){
                if(MemoConstants.KEY_IMAGE.equals(data.getKey())){
                    if(!imageFlag){
                        firstImagePath = data.getVal();
                        imageFlag = true;
                    }
                }else if(MemoConstants.KEY_TEXT.equals(data.getKey())){
                    if(!titleFlag){
                        title = data.getVal();
                        titleFlag = true;
                    }else if(!contentFlag){
                        firstContent = data.getVal();
                        contentFlag = true;
                    }
                }
            }
        }
        if(!imageFlag){
            firstImagePath = "";
        }
        mEditDataList = editDataList;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    @Override
    public String toString() {
        return "MemoListItemModel{" +
                "createMonth='" + createMonth + '\'' +
                ", createTime='" + createTime + '\'' +
                ", title='" + title + '\'' +
                ", firstContent='" + firstContent + '\'' +
                ", firstImagePath='" + firstImagePath + '\'' +
                ", mEditDataList=" + mEditDataList +
                '}';
    }
}
