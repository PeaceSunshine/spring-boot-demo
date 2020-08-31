package com.config;

import com.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author lx
 * @Date 2020/8/25
 * @Description
 **/
@Configuration
public class MyWebConfig implements WebMvcConfigurer {
    
    @Autowired
    private AuthInterceptor authInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        //所有url路径进行拦截
        registry.addInterceptor(authInterceptor).addPathPatterns("/**");
    }
}
