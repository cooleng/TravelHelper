package com.xiaomo.travelhelper.model.circle;

import java.util.List;

/**
 * 文件上传结果
 */

public class UploadResult {

    private String msg;
    private List<String> data;
    private boolean success;
    private int status;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public List<String> getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getStatus() {
        return status;
    }
}
