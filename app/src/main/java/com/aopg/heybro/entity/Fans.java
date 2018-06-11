package com.aopg.heybro.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by 王伟健 on 2018-06-08.
 */

public class Fans extends DataSupport {

    private String userCode;
    private String userFanCode;
    private String userFanNickName;
    private String userNode;



    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserFanCode() {
        return userFanCode;
    }

    public void setUserFanCode(String userFanCode) {
        this.userFanCode = userFanCode;
    }

    public String getUserFanNickName() {
        return userFanNickName;
    }

    public void setUserFanNickName(String userFanNickName) {
        this.userFanNickName = userFanNickName;
    }

    public String getUserNode() {
        return userNode;
    }

    public void setUserNode(String userNode) {
        this.userNode = userNode;
    }
}
