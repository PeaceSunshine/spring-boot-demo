package com.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.user.api.ICartService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author lx
 * @Date 2020/8/25
 * @Description
 **/
@Component
public class SSOHandler {
    
    @Reference
    private ICartService cartService;
    
    @RabbitListener(queues = "sso-queue-cart")
    @RabbitHandler
    public void proccess(Map<String,String> param){
        String nologinKey = param.get("nologinKey");
        String loginKey = param.get("loginKey");
        //合并购物车
        cartService.merge(nologinKey,loginKey);
    }
}
