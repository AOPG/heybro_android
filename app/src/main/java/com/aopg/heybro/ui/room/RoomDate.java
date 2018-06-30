package com.aopg.heybro.ui.room;


/**
 *  Created by 王攀 on 2018/6/15.
 *  房间在数据库中查找的数据
 */


public class RoomDate {

    private String roomId;
    private String  roomName;
    private String roomNum;
    private String  roomPro;
    private String roomPass;
    private String roomPassSet;
    private String roomLat;
    private String roomLng;

    public String getRoomLat() {
        return roomLat;
    }

    public void setRoomLat(String roomLat) {
        this.roomLat = roomLat;
    }

    public String getRoomLng() {
        return roomLng;
    }

    public void setRoomLng(String roomLng) {
        this.roomLng = roomLng;
    }

    public String getRoomPassSet() {
        return roomPassSet;
    }

    public void setRoomPassSet(String roomPassSet) {
        this.roomPassSet = roomPassSet;
    }

    public String getRoomPass() {
        return roomPass;
    }

    public void setRoomPass(String roomPass) {
        this.roomPass = roomPass;
    }

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

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getRoomPro() {
        return roomPro;
    }

    public void setRoomPro(String roomPro) {
        this.roomPro = roomPro;
    }
}
