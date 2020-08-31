package com.api.email;

import common.pojo.ResultBean;

/**
 * @Author lx
 * @Date 2020/6/23
 * @Description
 **/
public interface IEmailService {


    /**
     * 发送生日祝福
     * @param to 收件人
     * @param username 用户名
     * @return
     */
    public ResultBean sendBirthday(String to,String username);

    /**
     * 发送激活邮件
     * @param to 收件人
     * @param username 用户名
     * @return
     */
    public ResultBean sendActive(String to,String username);
}
