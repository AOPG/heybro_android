package com.aopg.heybro.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by 王伟健 on 2018-06-13.
 */

public class RoomUserInfo extends DataSupport {
    private String roomId;
    private String userCode;
    private String userNote;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }
}
