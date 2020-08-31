package com.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.user.api.ICartService;
import common.pojo.ResultBean;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @Author lx
 * @Date 2020/8/25
 * @Description
 **/
@RestController
@RequestMapping("cart")
public class CartController {
    
    @Reference
    private ICartService cartService;

    @GetMapping("add/{productId}/{count}")
    public ResultBean add(@PathVariable("productId") Long productId,
                          @PathVariable("count") Integer count,
                          @CookieValue(name = "cart_token",required = false) String cartToken,
                          HttpServletRequest request,
                          HttpServletResponse response){

        String userToken = (String) request.getAttribute("user");
        if(userToken != null){
            //说明已登录
            return cartService.add(userToken,productId,count);
        }

        //未登录，创建一个uuid
        if(cartToken == null){
            cartToken = UUID.randomUUID().toString();
        }
        //写cookie到客户端,更新有效期
        reflushCookie(cartToken, response);
        return cartService.add(cartToken,productId,count);
    }
    
    private void reflushCookie(@CookieValue(name="cart_token",required = false) String cartToken, HttpServletResponse response){
        Cookie cookie = new Cookie("cart_token", cartToken);
        cookie.setPath("/");
        cookie.setDomain("localhot");
        cookie.setMaxAge(15*24*60*60);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
