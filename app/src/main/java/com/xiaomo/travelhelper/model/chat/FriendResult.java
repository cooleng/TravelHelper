package com.xiaomo.travelhelper.model.chat;


import android.text.TextUtils;

import com.xiaomo.travelhelper.view.contact.Cn2Spell;

import java.util.List;


public class FriendResult {

    private String msg;
    private List<User> data;
    private boolean success;
    private int status;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<User> data) {
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

    public List<User> getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getStatus() {
        return status;
    }

    public static class User implements Comparable<User>{

        private String area;
        private String img;
        private String description;
        private int id;
        private String account;
        private String email;
        private String username;

        private String pinyin; // 姓名对应的拼音
        private String firstLetter; // 拼音的首字母

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public String getFirstLetter() {
            return firstLetter;
        }

        public void setFirstLetter(String firstLetter) {
            this.firstLetter = firstLetter;
        }

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


        @Override
        public int compareTo(User another) {

            if (firstLetter.equals("#") && !another.getFirstLetter().equals("#")) {
                return 1;
            } else if (!firstLetter.equals("#") && another.getFirstLetter().equals("#")){
                return -1;
            } else {
                return pinyin.compareToIgnoreCase(another.getPinyin());
            }
        }

        public void initPinyin(){

            if(!TextUtils.isEmpty(username)){
                pinyin = Cn2Spell.getPinYin(username); // 根据姓名获取拼音
                firstLetter = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
                if (!firstLetter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
                    firstLetter = "#";
                }
            }
        }

    }
}
