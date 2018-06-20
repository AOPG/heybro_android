package com.aopg.heybro.entity;

/**
 * Created by 壑过忘川 on 2018/6/19.
 * 打球房间信息
 */

public class BasketRoomInfo {
    //房间id
    private int roomId;
    //房间名称
    private String roomName;
    //类型
    private int type;
    //模式
    private String mode;
    //最低等级限制
    private int rateLow;
    //最高等级限制
    private int rateHigh;
    //人数限制
    private int num;
    //房间密码
    private String password;
    //房主编码
    private String master;

    public BasketRoomInfo() {

    }
    public BasketRoomInfo(int roomId, String roomName, int type, String mode, int rateLow, int rateHigh, int num, String password, String master) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.type = type;
        this.mode = mode;
        this.rateLow = rateLow;
        this.rateHigh = rateHigh;
        this.num = num;
        this.password = password;
        this.master = master;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getRateLow() {
        return rateLow;
    }

    public void setRateLow(int rateLow) {
        this.rateLow = rateLow;
    }

    public int getRateHigh() {
        return rateHigh;
    }

    public void setRateHigh(int rateHigh) {
        this.rateHigh = rateHigh;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }
}
