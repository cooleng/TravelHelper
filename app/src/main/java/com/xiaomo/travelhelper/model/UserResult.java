package com.xiaomo.travelhelper.model;

/**
 * 注册结果
 */

public class UserResult {

    private String msg;
    private DataEntity data;
    private boolean success;
    private int status;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(DataEntity data) {
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

    public DataEntity getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getStatus() {
        return status;
    }

    public class DataEntity {

        private String area;
        private String password;
        private String img;
        private String createTime;
        private String updateTime;
        private String id;
        private String account;
        private String email;
        private String username;

        public void setArea(String area) {
            this.area = area;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public void setId(String id) {
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

        public String getPassword() {
            return password;
        }

        public String getImg() {
            return img;
        }

        public String getCreateTime() {
            return createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public String getId() {
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
