package com.xiaomo.travelhelper.model.memo;

import org.litepal.crud.DataSupport;

/**
 * 富文本数据模型
 */

public class MemoEditData extends DataSupport {

    private String inputStr;
    private String imagePath;

    private MemoListItemModel model;

    public String getInputStr() {
        return inputStr;
    }

    public void setInputStr(String inputStr) {
        this.inputStr = inputStr;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public MemoListItemModel getModel() {
        return model;
    }

    public void setModel(MemoListItemModel model) {
        this.model = model;
    }
}
