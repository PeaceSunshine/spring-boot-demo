package com.api.user;

import com.entity.TUser;
import common.base.IBaseService;
import common.pojo.ResultBean;
import common.pojo.TResultBean;

/**
 * @Author lx
 * @Date 2020/8/3
 * @Description
 **/
public interface IUserService extends IBaseService<TUser> {
     ResultBean checkUserNameIsExists(String username);
     ResultBean checkPhoneIsExists(String phone);
     ResultBean checkEmailIsExists(String email);

     ResultBean generateCode(String identification);

     TResultBean checkLogin(TUser user);

    ResultBean checkIsLogin(String uuid);

    //目前分析得到的结论
    //添加用户，是否可以用默认的实现？
    //可以。

    //激活用户，改变用户的状态值，也不需要写
}
