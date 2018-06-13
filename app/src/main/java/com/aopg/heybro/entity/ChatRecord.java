package com.aopg.heybro.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by 王伟健 on 2018-06-08.
 */

public class ChatRecord extends DataSupport {

    private String userCode;
    //对方code
    private String theOrtherCode;
    //时间戳
    private long Date;
    private boolean isToMe;
    private String message;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getTheOrtherCode() {
        return theOrtherCode;
    }

    public void setTheOrtherCode(String theOrtherCode) {
        this.theOrtherCode = theOrtherCode;
    }

    public long getDate() {
        return Date;
    }

    public void setDate(long date) {
        Date = date;
    }

    public boolean isToMe() {
        return isToMe;
    }

    public void setToMe(boolean toMe) {
        isToMe = toMe;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
