package com.qf.qfv9cart.controller;

import com.alibaba.dubbo.common.serialize.java.CompactedObjectInputStream;
import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.api.cart.ICartService;
import com.qf.v9.common.pojo.ResultBean;
import com.qf.v9.entity.TUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/25
 */
@Controller
@RequestMapping("cart")
public class CartController {

    //前后端分离的方式，约定接口
    //并行开发
    //开发接口
    //前端，伪造假数据
    //联调

    @Reference
    private ICartService cartService;

    @RequestMapping("add/{productId}/{count}")
    @ResponseBody
    public ResultBean add(@PathVariable("productId") Long productId,
                          @PathVariable("count") Integer count,
                          @CookieValue(name = "user_cart",required = false) String uuid,
                          HttpServletResponse response,
                          HttpServletRequest request){

        //查看当前用户的登录状态
        //user:cart:userId
        //user:cart:uuid
        //TODO 但凡这些常用的字符串，都需要通过常量类管理起来
        TUser user = (TUser) request.getAttribute("user");
        String key = "";
        if(user != null){
            key = new StringBuilder("user:cart:").append(user.getId()).toString();
        }else{
            if(uuid == null){
                uuid = UUID.randomUUID().toString();
            }
            key = new StringBuilder("user:cart:").append(uuid).toString();
        }
        //写cookie到客户端
        flushCookie(uuid, response);
        return cartService.add(key.toString(),productId,count);
    }

    private void flushCookie(String uuid, HttpServletResponse response) {
        Cookie cookie = new Cookie("user_cart",uuid);
        cookie.setPath("/");
        cookie.setDomain("qf.com");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(30*24*60*60);
        response.addCookie(cookie);
    }

    @RequestMapping("list")
    @ResponseBody
    public ResultBean list(@CookieValue(name = "user_cart",required = false) String uuid,
                           HttpServletResponse response,
                           HttpServletRequest request){
        //定位--走到哪了
        //查看当前用户的登录状态
        //user:cart:userId
        //user:cart:uuid
        TUser user = (TUser) request.getAttribute("user");
        String key = "";
        if(user != null){
            key = new StringBuilder("user:cart:").append(user.getId()).toString();
        }else{
            if(uuid == null){
                return ResultBean.error("购物车暂无商品信息！");
            }
            key = new StringBuilder("user:cart:").append(uuid).toString();
        }
        //写cookie到客户端
        flushCookie(uuid, response);
        return cartService.list(key.toString());
    }

    @RequestMapping("update/{productId}/{count}")
    @ResponseBody
    public ResultBean update(@PathVariable("productId") Long productId,
                             @PathVariable("count") Integer count,
                             @CookieValue(name = "user_cart",required = false) String uuid,
                             HttpServletResponse response,
                             HttpServletRequest request){
        TUser user = (TUser) request.getAttribute("user");
        String key = "";
        if(user != null){
            key = new StringBuilder("user:cart:").append(user.getId()).toString();
        }else{
            if(uuid == null){
                return ResultBean.error("购物车暂无商品信息！");
            }
            key = new StringBuilder("user:cart:").append(uuid).toString();
        }
        ResultBean resultBean = cartService.updateCount(key.toString(), productId, count);
        //刷cookie
        flushCookie(uuid,response);
        return resultBean;
    }

    @RequestMapping("del/{productId}")
    @ResponseBody
    public ResultBean del(@PathVariable("productId") Long productId,
                          @CookieValue(name = "user_cart",required = false) String uuid,
                          HttpServletResponse response,
                          HttpServletRequest request){
        TUser user = (TUser) request.getAttribute("user");
        String key = "";
        if(user != null){
            key = new StringBuilder("user:cart:").append(user.getId()).toString();
        }else{
            if(uuid == null){
                return ResultBean.error("购物车暂无商品信息！");
            }
            key = new StringBuilder("user:cart:").append(uuid).toString();
        }
        ResultBean resultBean = cartService.del(key.toString(), productId);
        flushCookie(uuid,response);
        return resultBean;
    }

    @RequestMapping("merge")
    @ResponseBody
    public ResultBean merge(@CookieValue(name = "user_cart",required = false) String uuid,
                            HttpServletResponse response,
                            HttpServletRequest request){
        //
        TUser user = (TUser) request.getAttribute("user");
        if(user == null){
            return ResultBean.error("未登录状态！");
        }
        if(uuid == null || "".equals(uuid)){
            return ResultBean.error("不存在未登录购物车,无需合并");
        }
        //
        String loginKey = new StringBuilder("user:cart:").append(user.getId()).toString();
        String noLoginKey = new StringBuilder("user:cart:").append(uuid).toString();
        ResultBean resultBean = cartService.merge(noLoginKey, loginKey);
        //TODO 清除掉cookie
        //重新写cookie，设置有效期为0，就是删除
        return resultBean;
    }
}
