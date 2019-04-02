package com.qf.qfv9sso.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.qf.api.search.IUserService;
import com.qf.v9.common.pojo.ResultBean;
import com.qf.v9.common.util.HttpClientUtils;
import com.qf.v9.entity.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/22
 */
@Controller
@RequestMapping("sso")
public class SSOController {

    @Reference
    private IUserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    //1，展示登录页
    @RequestMapping("login")
    public String showLogin(HttpServletRequest request){
        //获取到从哪而来
        String referer = request.getHeader("Referer");
        if(referer == null || "".equals(referer)){
            referer = request.getParameter("referer");
        }
        request.setAttribute("referer",referer);
        return "login";
    }

    //2.认证
    @RequestMapping("checkLogin")
    public String checkLogin(TUser user, HttpServletResponse response,HttpServletRequest request,
                             @CookieValue(name = "user_cart",required = false) String userCartToken){
        //1.查数据库
        TUser currentUser = userService.checkLogin(user);
        //2.
        if(currentUser == null){
            return "login";
        }
        //3.
        //3.1 生成唯一的标识
        String uuid = UUID.randomUUID().toString();
        //3.2 构造一个cookie
        Cookie cookie = new Cookie("user_token",uuid);
        cookie.setPath("/");
        //设置成父域名，这样所有的子域名系统都可以共享到
        //sso.qf.com
        cookie.setDomain("qf.com");
        //基于安全控制，只允许通过后端来获取到cookie的信息
        //不能在前端通过脚本获取到cookie document.cookies
        cookie.setHttpOnly(true);
        //3.3 redis中保存凭证信息
        StringBuilder redisKey = new StringBuilder("user:token:").append(uuid);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(redisKey.toString(),currentUser);
        //设置有效期
        redisTemplate.expire(redisKey.toString(),30, TimeUnit.MINUTES);
        response.addCookie(cookie);

        String referer = request.getParameter("referer");

        //登录成功之后，应该是调用购物车合并的接口
        //需要有一个工具，来模拟浏览器发送http请求
        //HttpClient---> Apache
        StringBuilder value = new StringBuilder("user_token=");
        value.append(uuid);
        value.append(";");
        value.append("user_cart=");
        value.append(userCartToken);

        Map<String,String> params = new HashMap<>();
        params.put("Cookie",value.toString());
        HttpClientUtils.doGetWithHeaders("http://cart.qf.com:9099/cart/merge",params);
        //这样的话，默认是不携带cookie信息的
        //Cookie: user_cart=f224334a-25f9-45de-9172-3ab957a3fe94; user_token=dff71675-5ced-4ea8-a97b-5bb775401337


        if(referer != null && !"".equals(referer)){
            return "redirect:"+referer;
        }
        return "redirect:http://localhost:9091";
    }


    @RequestMapping("checkIsLogin")
    @ResponseBody
    @CrossOrigin(origins = "*",allowCredentials = "true")
    public ResultBean checkIsLogin(@CookieValue(name = "user_token",required = false) String uuid){
        //apache httpclient
        //1.获取到保存在cookie中的uuid
        if(uuid == null){
            return ResultBean.error("用户未登录");
        }
        return userService.checkIsLogin(uuid);
    }

    @RequestMapping("checkIsLoginForJsonp")
    @ResponseBody
    public String checkIsLoginForJsonp(@CookieValue(name = "user_token",required = false) String uuid,String callback){
        //apache httpclient
        //1.获取到保存在cookie中的uuid
        if(uuid == null){
            ResultBean resultBean = ResultBean.error("用户未登录");
            Gson gson = new Gson();
            String json = gson.toJson(resultBean);
            return callback+"("+json+")";
        }
        //
        StringBuilder redisKey = new StringBuilder("user:token:").append(uuid);
        //
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        TUser currentUser = (TUser) redisTemplate.opsForValue().get(redisKey.toString());
        if(currentUser != null){
            //刷新凭证的有效期
            //30分钟 20->30
            redisTemplate.expire(redisKey.toString(),30,TimeUnit.MINUTES);
            ResultBean resultBean = ResultBean.success(currentUser.getUsername());
            Gson gson = new Gson();
            String json = gson.toJson(resultBean);
            return callback+"("+json+")";
        }
        ResultBean resultBean = ResultBean.error("用户未登录");
        Gson gson = new Gson();
        String json = gson.toJson(resultBean);
        return callback+"("+json+")";
    }

    @RequestMapping("logout")
    @ResponseBody
    public ResultBean logout(@CookieValue(name = "user_token",required = false) String uuid,
                             HttpServletResponse response){
        if(uuid == null){
            return ResultBean.error("注销失败");
        }
        //1.删除cookie
        Cookie cookie = new Cookie("user_token",uuid);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setDomain("qf.com");
        //让cookie失效
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        //2.删除redis的凭证
        StringBuilder redisKey = new StringBuilder("user:token:").append(uuid);
        //
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.delete(redisKey.toString());
        return ResultBean.success("注销成功！");
    }
}
