package com.qf.qfv9order.config;

import com.qf.qfv9order.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/27
 */
@Configuration
public class WebConfig implements WebMvcConfigurer{

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(authInterceptor).addPathPatterns("/**");
    }
}
