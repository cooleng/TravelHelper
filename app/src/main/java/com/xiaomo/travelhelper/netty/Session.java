package com.xiaomo.travelhelper.netty;

/**
 * author: mojiale66@163.com
 * date:   2018/4/28
 * description: 会话管理接口
 */
public interface Session {

    /**
     * 获取会话绑定对象
     * @return
     */
    Object getAttachment();

    /**
     * 设置会话绑定对象
     * @return
     */
    void setAttachment(Object attachment);

    /**
     * 移除会话绑定对象
     * @return
     */
    void removeAttachment();

    /**
     * 向会话中写入消息
     * @param message
     */
    void write(Object message);

    /**
     * 判断会话是否在连接中
     * @return
     */
    boolean isConnected();

    /**
     * 关闭
     * @return
     */
    void close();


}
