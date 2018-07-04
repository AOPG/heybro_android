package com.aopg.heybro.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by 王伟健 on 2018-06-30.
 */

public class UserConversation extends DataSupport {

    private String userCode;
    private String userConversationCode;
    private Integer unReadNum;
    private long date;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserConversationCode() {
        return userConversationCode;
    }

    public void setUserConversationCode(String userConversationCode) {
        this.userConversationCode = userConversationCode;
    }

    public Integer getUnReadNum() {
        return unReadNum;
    }

    public void setUnReadNum(Integer unReadNum) {
        this.unReadNum = unReadNum;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
