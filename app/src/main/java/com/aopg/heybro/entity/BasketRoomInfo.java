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
    //等级限制
    private String rate;
    //人数限制
    private int num;
    //房间密码
    private String password;
    //房主信息
    private User master = new User();

    public BasketRoomInfo() {

    }
    public BasketRoomInfo(int roomId, String roomName, int type, String mode, String rate, int num,String password,User master) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.type = type;
        this.mode = mode;
        this.rate = rate;
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
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

    public User getMaster() {
        return master;
    }

    public void setMaster(User master) {
        this.master = master;
    }
}
