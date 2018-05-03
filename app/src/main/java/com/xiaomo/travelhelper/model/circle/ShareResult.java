package com.xiaomo.travelhelper.model.circle;

import java.util.List;

/**
 * Created by mojiale on 2018/4/11.
 */

public class ShareResult {

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

        private String imgUrl;
        private String area;
        private List<CommentListEntity> commentList;
        private String img;
        private String createTime;
        private int id;
        private String account;
        private String content;
        private String username;

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public void setCommentList(List<CommentListEntity> commentList) {
            this.commentList = commentList;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public String getArea() {
            return area;
        }

        public List<CommentListEntity> getCommentList() {
            return commentList;
        }

        public String getImg() {
            return img;
        }

        public String getCreateTime() {
            return createTime;
        }

        public int getId() {
            return id;
        }

        public String getAccount() {
            return account;
        }

        public String getContent() {
            return content;
        }

        public String getUsername() {
            return username;
        }

        public class CommentListEntity {

            private String commentorId;
            private String content;
            private String username;

            public void setCommentorId(String commentorId) {
                this.commentorId = commentorId;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getCommentorId() {
                return commentorId;
            }

            public String getContent() {
                return content;
            }

            public String getUsername() {
                return username;
            }
        }
    }
}
