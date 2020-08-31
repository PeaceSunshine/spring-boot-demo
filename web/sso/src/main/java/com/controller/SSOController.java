package com.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.api.user.IUserService;
import com.entity.TUser;
import common.pojo.ResultBean;
import common.pojo.TResultBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author lx
 * @Date 2020/8/19
 * @Description
 **/
@RequestMapping("sso")
@Controller
public class SSOController {

    @Reference
    private IUserService userService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
    *
    * 登录跳转
    * */
    @RequestMapping("login")
    public String login(HttpServletRequest request){
        String referer = request.getHeader("Referer");
        request.setAttribute("referer",referer);
        return "login";
    }


    /**
    * 认证功能:认证成功生成令牌
    * */
    @PostMapping("checkLogin")
    public String checkLogin(TUser user, HttpServletRequest request,HttpServletResponse response) {
        TResultBean resultBean = userService.checkLogin(user);

        //2.如果用户登录信息正确，则在服务器保存凭证信息
        if ("200".equals(resultBean.getStatusCode())) {
            //写cookie给客户端，保存凭证
            String uuid = ((Map<String,String>) resultBean.getData()).get("jwtToken");
            //创建cookie对象
            Cookie cookie = new Cookie("user_token", uuid);
            cookie.setPath("/");
            //设置cookie的域名为父域，这样父域下的所有子系统都可以访问cookie
            //cookie.setDomain("qf.com");
            cookie.setHttpOnly(true);
            //3.将cookie写道客户端
            response.addCookie(cookie);
            //获取referer
            String referer = request.getParameter("referer");
            if(!StringUtils.isEmpty(referer)){
                return "redirect:"+referer;
            }

        }
        return "redirect:http://localhost:9091";
    }

    /**
    *
    * 检查是否登录:cookie中是否存在用户对应令牌
    * */
    @GetMapping("checkIsLogin")
    @CrossOrigin(origins = "*",allowCredentials = "true")
    @ResponseBody
    public ResultBean checkIsLogin(@CookieValue(name = "user_token",required = false) String uuid){
        if(uuid!=null){
            //cookie存的的是HashMap对象，checkIsLogin解析的是其中的jwtToken值？
            ResultBean resultBean = userService.checkIsLogin(uuid);
            return  resultBean;
        }
        return ResultBean.error(null);
    }

    /**
     *
     * 注销:删除cookie
     * */
    @GetMapping("logout")
    @ResponseBody
    public ResultBean logout(@CookieValue(name = "user_token",required = false) String token,HttpServletResponse response){
        if(token!=null){
            Cookie cookie = new Cookie("user_token",token);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            //cookie.setDomain("qf.com");2.清除cookie，设置有效期为0
            cookie.setMaxAge(0);
            //3.将cookie写回客户端
            response.addCookie(cookie);
        }
        return ResultBean.success("ok");
    }

}

