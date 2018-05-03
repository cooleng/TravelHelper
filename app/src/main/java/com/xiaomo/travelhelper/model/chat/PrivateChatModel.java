package com.xiaomo.travelhelper.model.chat;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * 聊天内容数据模型
 */

public class PrivateChatModel extends DataSupport{

    private String fromAccount;
    private String fromUsername;
    private String fromImg;
    private String toAccount;
    private String toUsername;
    private String toImg;
    private String content;
    private Date time;
    private Integer flag;

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getFromImg() {
        return fromImg;
    }

    public void setFromImg(String fromImg) {
        this.fromImg = fromImg;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public String getToImg() {
        return toImg;
    }

    public void setToImg(String toImg) {
        this.toImg = toImg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public long getId(){
        return super.getBaseObjId();
    }

    @Override
    public String toString() {
        return "PrivateChatModel{" +
                "fromAccount='" + fromAccount + '\'' +
                ", fromUsername='" + fromUsername + '\'' +
                ", fromImg='" + fromImg + '\'' +
                ", toAccount='" + toAccount + '\'' +
                ", toUsername='" + toUsername + '\'' +
                ", toImg='" + toImg + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", flag=" + flag +
                '}';
    }
}
