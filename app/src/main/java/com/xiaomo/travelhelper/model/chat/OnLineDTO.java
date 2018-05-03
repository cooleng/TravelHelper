package com.xiaomo.travelhelper.model.chat;


import com.xiaomo.travelhelper.netty.AbstractRequestSerializer;

/**
 * author: mojiale66@163.com
 * date:   2018/4/28
 * description: 在线用户DTO
 */
public class OnLineDTO extends AbstractRequestSerializer {

    private String username;

    private String account;


    @Override
    protected void read() {
        this.account = readString();
        this.username = readString();
    }

    @Override
    protected void write() {
        writeString(account);
        writeString(username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "OnLineDTO{" +
                "username='" + username + '\'' +
                ", account='" + account + '\'' +
                '}';
    }
}
