package com.aopg.heybro.entity;

import org.litepal.crud.DataSupport;


/**
 * Created by 王伟健 on 2018-06-13.
 */

public class RoomInfo extends DataSupport {
    //房间Id
    private String roomId;
    //房间名称
    private String roomName;


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

}
