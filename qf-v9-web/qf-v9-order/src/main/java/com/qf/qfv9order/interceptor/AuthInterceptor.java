package com.qf.qfv9order.interceptor;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.api.search.IUserService;
import com.qf.v9.common.pojo.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/27
 */
@Component
public class AuthInterceptor implements HandlerInterceptor{

    @Reference
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //必须是登录之后，才能进行订单的相关操作
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if("user_token".equals(cookie.getName())){
                    String uuid = cookie.getValue();
                    ResultBean resultBean = userService.checkIsLogin(uuid);
                    if(resultBean.getStatusCode() == 200){
                        request.setAttribute("user",resultBean.getData());
                        return true;
                    }
                }
            }
        }
        //跳转到登录页展示
        //不会像浏览器一样携带Referer头部信息过去
        //基于API编程
        System.out.println(request.getRequestURL().toString());
        response.sendRedirect("http://sso.qf.com:9098/sso/login?referer="+request.getRequestURL().toString());
        //不放行
        return false;
    }
}
