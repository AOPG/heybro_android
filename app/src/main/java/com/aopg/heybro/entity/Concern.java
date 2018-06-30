package com.aopg.heybro.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by 王伟健 on 2018-06-08.
 */

public class Concern extends DataSupport {

    private String userCode;
    private String userConcernCode;
    private String userConcernNickName;
    //备注
    private String userNote;

    private String userPortrait;

    public String getUserPortrait() {
        return userPortrait;
    }

    public void setUserPortrait(String userPortrait) {
        this.userPortrait = userPortrait;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserConcernCode() {
        return userConcernCode;
    }

    public void setUserConcernCode(String userConcernCode) {
        this.userConcernCode = userConcernCode;
    }

    public String getUserConcernNickName() {
        return userConcernNickName;
    }

    public void setUserConcernNickName(String userConcernNickName) {
        this.userConcernNickName = userConcernNickName;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }
}
