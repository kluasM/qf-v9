package com.qf.qfv9cart.interceptor;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.api.search.IUserService;
import com.qf.v9.common.pojo.ResultBean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/26
 */
@Component
public class AuthInterceptor implements HandlerInterceptor{

    //JDK1.8之前，接口不允许有实现
    //所以，我们一般会实现一个适配器类，这个类就是提供对这个接口的空实现
    //我们的类继承这个适配器类，然后可以选择性实现某些方法

    //JDK1.8之后，接口允许有默认实现
    //适配器的这种做法就退出了

    @Reference
    private IUserService userService;

    //前置拦截
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        //获取到用户的登录状态
        //调用单点登录系统提供的接口
        //如果是调用Controller级别的接口，HttpClient
        //改造，抽取出Service，这样可以继续用RPC的方式来调用
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return true;
        }
        for (Cookie cookie : cookies) {
            if("user_token".equals(cookie.getName())){
                String uuid = cookie.getValue();
                ResultBean resultBean = userService.checkIsLogin(uuid);
                if(resultBean.getStatusCode() == 200){
                    //已经登录
                    request.setAttribute("user",resultBean.getData());
                    return true;
                }
            }
        }
        //不管登录不登录都可以操作购物车
        return true;
    }
}
