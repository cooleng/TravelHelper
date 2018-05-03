package com.xiaomo.travelhelper.model.chat;


import com.xiaomo.travelhelper.netty.AbstractRequestSerializer;

/**
 * author: mojiale66@163.com
 * date:   2018/4/28
 * description: 私聊请求DTO
 */
public class PrivateChatRequestDTO extends AbstractRequestSerializer {

    private String sendAccount;
    private String targetAccount;
    private String content;

    public String getSendAccount() {
        return sendAccount;
    }

    public void setSendAccount(String sendAccount) {
        this.sendAccount = sendAccount;
    }

    public String getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(String targetAccount) {
        this.targetAccount = targetAccount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    protected void read() {
        this.sendAccount = readString();
        this.targetAccount = readString();
        this.content = readString();
    }

    @Override
    protected void write() {
        writeString(this.sendAccount);
        writeString(this.targetAccount);
        writeString(this.content);
    }

    @Override
    public String toString() {
        return "PrivateChatRequestDTO{" +
                "sendAccount='" + sendAccount + '\'' +
                ", targetAccount='" + targetAccount + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
