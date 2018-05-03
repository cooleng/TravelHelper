package com.xiaomo.travelhelper.model.memo;

import org.litepal.crud.DataSupport;

/**
 * 富文本数据模型
 */

public class MemoEditData extends DataSupport {

    private String key;
    private String val;

    private MemoListItemModel model;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public MemoListItemModel getModel() {
        return model;
    }

    public void setModel(MemoListItemModel model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "MemoEditData{" +
                "key='" + key + '\'' +
                ", val='" + val + '\'' +
                ", model=" + model +
                '}';
    }
}
