package com.aopg.heybro.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by 王伟健 on 2018-06-13.
 */

public class ChatRoomRecord extends DataSupport implements Serializable {
    //房间ID
    private String roomId;
    //房间名称
    private String roomName;
    //用户code
    private String userCode;
    //消息内容
    private String message;
    //时间戳
    private long Date;


    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDate() {
        return Date;
    }

    public void setDate(long date) {
        Date = date;
    }
}
