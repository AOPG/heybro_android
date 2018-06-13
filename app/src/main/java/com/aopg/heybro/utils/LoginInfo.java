package com.aopg.heybro.utils;

import com.aopg.heybro.entity.User;

/**
 * Created by 王伟健 on 2018-06-11.
 */

public class LoginInfo {
    public static User user = new User();
    //0为未创建FragementMy界面，1为已创建
    public static Integer FragmentMYISCREATE = 0;
    //0为未登录IM，1为已登录IM
    public static Integer ISLOGINIM = 0;
}
