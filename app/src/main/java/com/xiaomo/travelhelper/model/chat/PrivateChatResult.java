package com.xiaomo.travelhelper.model.chat;

import java.util.List;

/**
 * 私聊响应结果数据模型
 */

public class PrivateChatResult {

    private String msg;
    private List<DataEntity> data;
    private boolean success;
    private int status;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<DataEntity> data) {
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

    public List<DataEntity> getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getStatus() {
        return status;
    }

    public class DataEntity {

        private String toAccount;
        private String toUsername;
        private int module;
        private int length;
        private String fromAccount;
        private String fromImg;
        private String content;
        private String bytes;
        private int cmd;
        private String time;
        private String fromUsername;
        private String toImg;
        private int statusCode;

        public void setToAccount(String toAccount) {
            this.toAccount = toAccount;
        }

        public void setToUsername(String toUsername) {
            this.toUsername = toUsername;
        }

        public void setModule(int module) {
            this.module = module;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public void setFromAccount(String fromAccount) {
            this.fromAccount = fromAccount;
        }

        public void setFromImg(String fromImg) {
            this.fromImg = fromImg;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setBytes(String bytes) {
            this.bytes = bytes;
        }

        public void setCmd(int cmd) {
            this.cmd = cmd;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setFromUsername(String fromUsername) {
            this.fromUsername = fromUsername;
        }

        public void setToImg(String toImg) {
            this.toImg = toImg;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public String getToAccount() {
            return toAccount;
        }

        public String getToUsername() {
            return toUsername;
        }

        public int getModule() {
            return module;
        }

        public int getLength() {
            return length;
        }

        public String getFromAccount() {
            return fromAccount;
        }

        public String getFromImg() {
            return fromImg;
        }

        public String getContent() {
            return content;
        }

        public String getBytes() {
            return bytes;
        }

        public int getCmd() {
            return cmd;
        }

        public String getTime() {
            return time;
        }

        public String getFromUsername() {
            return fromUsername;
        }

        public String getToImg() {
            return toImg;
        }

        public int getStatusCode() {
            return statusCode;
        }
    }
}
