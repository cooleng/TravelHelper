package com.xiaomo.travelhelper.model.chat;

import java.util.List;

/**
 * 添加朋友消息
 */

public class MsgFriendResult {

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
        /**
         * area : 广东深圳
         * img : 广东深圳
         * description : 我是莫家乐
         * id : 22
         * account : 13113288564
         * email : mojiale66@163.com
         * username : 小莫是个大傻瓜
         */
        private String area;
        private String img;
        private String description;
        private int id;
        private String account;
        private String email;
        private String username;

        public void setArea(String area) {
            this.area = area;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getArea() {
            return area;
        }

        public String getImg() {
            return img;
        }

        public String getDescription() {
            return description;
        }

        public int getId() {
            return id;
        }

        public String getAccount() {
            return account;
        }

        public String getEmail() {
            return email;
        }

        public String getUsername() {
            return username;
        }
    }
}
