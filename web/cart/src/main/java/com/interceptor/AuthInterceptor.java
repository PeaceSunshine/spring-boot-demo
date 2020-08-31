package com.interceptor;

import com.alibaba.dubbo.config.annotation.Reference;
import com.api.user.IUserService;
import common.pojo.ResultBean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author lx
 * @Date 2020/8/25
 * @Description
 **/
@Component
public class AuthInterceptor implements HandlerInterceptor {
    
    @Reference
    private IUserService userService;

    /**
     * 前置拦截
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.判断是否处于登录状态
        //2.如果处于已登录，将信息保存到request中
        //3.无论是否登录都放行
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if("user_token".equals(cookie.getName())){
                    String token = cookie.getValue();
                    ResultBean resultBean = userService.checkIsLogin(token);
                    if("200".equals(resultBean.getStatusCode())){
                        //当前用户已登录
                        request.setAttribute("Data",resultBean.getData());
                        return true;
                    }
                }
            }
        }
        return true;
    }
}
