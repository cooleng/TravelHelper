package com.xiaomo.travelhelper.model.chat;


import com.xiaomo.travelhelper.netty.AbstractResponseSerializer;

/**
 * author: mojiale66@163.com
 * date:   2018/4/28
 * description: 私聊响应DTO
 */
public class PrivateChatResponseDTO extends AbstractResponseSerializer {

     private String fromAccount;
     private String fromUsername;
     private String fromImg;
     private String content;
     private String toAccount;
     private String toUsername;
     private String toImg;
     private String time;


    @Override
    protected void read() {

        this.fromAccount = readString();
        this.fromUsername = readString();
        this.fromImg = readString();
        this.content = readString();
        this.toAccount = readString();
        this.toUsername = readString();
        this.toImg = readString();
        this.time = readString();

    }

    @Override
    protected void write() {
        writeString(fromAccount);
        writeString(fromUsername);
        writeString(fromImg);
        writeString(content);
        writeString(toAccount);
        writeString(toUsername);
        writeString(toImg);
        writeString(time);
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFromImg() {
        return fromImg;
    }

    public void setFromImg(String fromImg) {
        this.fromImg = fromImg;
    }

    public String getToImg() {
        return toImg;
    }

    public void setToImg(String toImg) {
        this.toImg = toImg;
    }

    @Override
    public String toString() {
        return "PrivateChatResponseDTO{" +
                "fromAccount='" + fromAccount + '\'' +
                ", fromUsername='" + fromUsername + '\'' +
                ", content='" + content + '\'' +
                ", toAccount='" + toAccount + '\'' +
                ", toUsername='" + toUsername + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
