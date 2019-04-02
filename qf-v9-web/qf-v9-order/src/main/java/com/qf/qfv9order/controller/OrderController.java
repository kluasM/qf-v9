package com.qf.qfv9order.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.qf.api.cart.ICartService;
import com.qf.v9.entity.TUser;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/27
 */
@Controller
@RequestMapping("order")
public class OrderController {

    //详情页
    //http://order.qf.com/order/confrim

    @Reference
    private ICartService cartService;

    @Autowired
    private AlipayClient alipayClient;

    @RequestMapping("confirm")
    public String confirm(HttpServletRequest request){
        //1，获取当前登录用户，从而获取地址信息
        TUser user = (TUser) request.getAttribute("user");
        //2，物流配送方式+支付方式
        //3，获取购物车信息
        String key = "";
        cartService.list(key);
        return "order_confirm";
    }

    @RequestMapping("create")
    public String create(){
        //orderService
        //生成订单的信息 主键回填
        //订单基础表+订单明细表 mybatis for
        //数据源头：购物车
        //跳转到待支付页面
        return "order_pay";
    }

    @RequestMapping("success")
    public String success(){
        System.out.println("支付宝官方说明，这个同步回调，不能说明支付成功！");
        return "success";
    }

    @RequestMapping("notify")
    public void notifyResult(HttpServletRequest request,HttpServletResponse response) throws AlipayApiException, IOException {
        PrintWriter out = response.getWriter();
        System.out.println("异步回调，可以通过这个接口来确认真正的支付情况");
        Map<String, String[]> sourceMap = request.getParameterMap();
        Map<String, String> targetMap = new HashMap<>(); //将异步通知中收到的所有参数都存放到map中
        //sourceMap->targetMap
        Set<Map.Entry<String, String[]>> entries = sourceMap.entrySet();
        for (Map.Entry<String, String[]> entry : entries) {
            StringBuilder value = new StringBuilder();
            String[] values = entry.getValue();
            for (int i = 0; i < values.length-1; i++) {
                value.append(values[i]).append(",");
            }
            value.append(values[values.length-1]);
            //
            targetMap.put(entry.getKey(),value.toString());
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(targetMap,
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwkz5NNzXt9IcV8sheHaYskJ7G1kOEAyR5cn5CUw0iVqHURqokImbvNAILwMqRy4forXJNKM4CcjbuhigTmMxx6CYnn6n1yE8BHW1VGeFoJP9zFMtwTZboHHLteHCmWD0QDjj9yqhGxuM9Il6vPX/gtcpe5fLDY6yvFr9vSlD3q60GCkOvOScpL1YKmdFU28A7tz6O4V/IUhZdwM4LOdZCCNpTKou75lFT1hUuTarMbl9nD40ntGv6FeY1QeDNEXMyTJa8yfUwjUOs/ixkvyfcVAa5GNLygKjg7IoZ3CSA06GIuZUF16cdatOfFGWclr/6Fx/x8ubaZzg9t6zgOCU3wIDAQAB",
                "utf-8",
                "RSA2"); //调用SDK验证签名
        if(signVerified){
            System.out.println("验签成功，这是来自支付宝给我们发送的消息");
            // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
            String trade_status = request.getParameter("trade_status");
            if("TRADE_SUCCESS".equals(trade_status)){
                System.out.println("收到的支付状态是成功！");
                //关键参数，获取到支付宝的流水号，商户订单号，支付金额
                String trade_no = request.getParameter("trade_no");
                String out_trade_no = request.getParameter("out_trade_no");
                //实收金额
                String receipt_amount = request.getParameter("receipt_amount");
                //需要拿着订单编号out_trade_no和订单金额receipt_amount
                //去跟我们数据表的数据做匹配
                //如果匹配，那可以更新订单状态为“已支付”
                //如果不匹配，记录订单的状态为“未支付”，备注金额存在问题
                //调用对账系统的接口，完成信息的录入
                out.write("success");
                out.flush();
            }else{
                System.out.println(trade_status);
            }
        }else{
            System.out.println("验签失败，忽略本次的请求处理");
            // TODO 验签失败则记录异常日志，并在response中返回failure.
            out.write("failure");
            out.flush();
        }
        //1,验签，确认这个消息是支付宝给我的
        //2,获取支付状态
        //3,对照业务数据（10001 8888 支付宝流水号 对账）
        //4,更新订单的状态为已支付
    }

    @RequestMapping("pay")
    public void pay(HttpServletRequest httpRequest,
                HttpServletResponse httpResponse,String orderNo) throws ServletException, IOException {
            //商户调用支付宝的接口，之后就是支付宝跟用户直接对接
            //SDK -AlipayClient
            AlipayClient alipayClient = new DefaultAlipayClient(
                "https://openapi.alipaydev.com/gateway.do",
                "2016091100489896",
                "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCp5PRqYxq+5MwSvauOmnUly3QGggIj0MQmimGUIrMxoWwdMWZVwvgFCdTCuZU4fLZariCyqMWFlt7UPEBCUdG4eMlqJaq6P0liSWo7FIGKjvnrJKCsY7jBUVqE+5b88vm/MOCyCQhJRddALmvIyyOz9mw2LnjSm1gMDBuAaEMe6GTh7v+/ZZFDXqEMMPkdyFRUMNH2TGEi4ZhMx5Q9c/jLpVCf773JKX5SNjme510cHK0JIdTGOEMZWwaKTPRDbUbdRGUJRCykwMovx7P917JqCTR1/Tn1oKB1oRYftN44TcCqg6yJ5o6RDV3tdNkTST3g0JM7PCf7yMGpawmhkfidAgMBAAECggEAePNv8kGF6/s1Me5oMYjS98pFWtC5t0fMLWf9GhouGppCPvJJWasJKYUbPFok2ucmZ55p1ueCX53OG6idUtmvBbawJeqVBi1CEEAV6eOqabtqPzZ/UNtZz/8cA2qdzo0osi2Y+gamKIwZNy8VOwkr7NX1qgkhqUZAw6rm9V7E0j4TMklaj62HAvVDQHAqS/i95i7uAOypcKYKTEW8fgQzQIIUcJXz77EeBpd+FD+D3I+dUwOlAM4jZVRizQ817uT4xcEwM9rISj66U8ckry6O3yHBFGUHvRFHPjRCFHjo99xxh+TX7TLHEP7cyLsVs9QluvtRT77WC1BJKrFe37WKYQKBgQDscojSIQ7anqQuZDNGwi+trttftHOjFRTvtNHvHev+hdTc9okqUJ1qLBr/Hi4fLtwC7tWOdAfpo4138p7GqPK2sLI90iJHzeud6g7v1Nep5UunIhw0pG1W1btivguXwmZWxg//AbeENgeTWnp5nVIKg69EOD8HzQKOipYqwC4ZWQKBgQC38YcbCjRJAKdMMIYE23plrSuA+UeGgNnAH/6u4rjaX67ViyIQetGFl1G2+pxY+JX4Gxf9in6AyCgDM7zBudNeo+vNlmD0F60cPKvdN4GLa4ZhcAEE9Ft0gz3/wSNShDS3Okql7rUJ82X6HUv4kP07rPgRXz5ubgv5LIYU8oos5QKBgFb2qSvfzL7LqkcFw3Y65Z5TuYuxqvZIQrMHJk90CeBLCMdcRYayr7LlFejPCNefPHw3q4QBWQeth9KeAZe/e8WFp4jlSIZwTkB+XZAIEGoEU5bdDNKvxycw5QKkM09VyJ4RqsB3uS9/T7n6hSFFo28Kj+PfbA9Kb65Nen2uwUXBAoGAdZWaDjj5r2kRiOzD3zsGBRUr6ChyBpM7n0tUnSsli8L6mlt0jSBV65YNkHCnnWBbTdzT+ymbMGvC2DksgpJtgeQ7ipKEq8SuzowiOSmQUDWOE59rp2UlSVfUPDeUgt0sbax/p47uf8vt9nLf66eTmaA6e7Bs0l7anJBPrfhVR9kCgYAlEXsdMTe9JrpjhdvYxHvto6OWyMPa36QGHMulXb6NYHfYDqvG8a4zJnTWmfP9/XwyyCL0GJN1VGCZKlnEQH6I+7P4jx/RsqRryQxs/Es8UiiKWTKcr9zeoVHKJXq6E6++TsVARG9g9RKJj0turAnUUmZU1CeZQzZ6xTYJkE02nw==",
                "json",
                "utf-8",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwkz5NNzXt9IcV8sheHaYskJ7G1kOEAyR5cn5CUw0iVqHURqokImbvNAILwMqRy4forXJNKM4CcjbuhigTmMxx6CYnn6n1yE8BHW1VGeFoJP9zFMtwTZboHHLteHCmWD0QDjj9yqhGxuM9Il6vPX/gtcpe5fLDY6yvFr9vSlD3q60GCkOvOScpL1YKmdFU28A7tz6O4V/IUhZdwM4LOdZCCNpTKou75lFT1hUuTarMbl9nD40ntGv6FeY1QeDNEXMyTJa8yfUwjUOs/ixkvyfcVAa5GNLygKjg7IoZ3CSA06GIuZUF16cdatOfFGWclr/6Fx/x8ubaZzg9t6zgOCU3wIDAQAB",
                "RSA2");//SHA256
            //获得初始化的AlipayClient
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
            alipayRequest.setReturnUrl("http://huangguizhao.natapp1.cc/order/success");
            alipayRequest.setNotifyUrl("http://huangguizhao.natapp1.cc/order/notify");//在公共参数中设置回跳和通知地址
            //订单编号+金额 对账
            /*alipayRequest.setBizContent("{" +
                    "    \"out_trade_no\":\""+orderNo+"\"," +
                    "    \"product_code\":\"123\"," +
                    "    \"total_amount\":8888.88," +
                    "    \"subject\":\"IphoneX 256G\"," +
                    "    \"body\":\"IphoneX 256G\"," +
                    "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"" +
                    "  }");//填充业务参数*/
            alipayRequest.setBizContent("{" +
                    "    \"out_trade_no\":\""+orderNo+"\"," +
                    "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                    "    \"total_amount\":88888.88," +
                    "    \"subject\":\"Iphone10 256G\"," +
                    "    \"body\":\"Iphone10 256G\"," +
                    "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"" +
                    "  }");//填充业务参数
            String form="";
            try {
                form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
            } catch (AlipayApiException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
            httpResponse.setContentType("text/html;charset=utf-8");
            httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();
    }

    //此处应该是在对账系统去实现
    @RequestMapping("getFile")
    public void getFile() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(
                "https://openapi.alipaydev.com/gateway.do",
                "2016091100489896",
                "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCp5PRqYxq+5MwSvauOmnUly3QGggIj0MQmimGUIrMxoWwdMWZVwvgFCdTCuZU4fLZariCyqMWFlt7UPEBCUdG4eMlqJaq6P0liSWo7FIGKjvnrJKCsY7jBUVqE+5b88vm/MOCyCQhJRddALmvIyyOz9mw2LnjSm1gMDBuAaEMe6GTh7v+/ZZFDXqEMMPkdyFRUMNH2TGEi4ZhMx5Q9c/jLpVCf773JKX5SNjme510cHK0JIdTGOEMZWwaKTPRDbUbdRGUJRCykwMovx7P917JqCTR1/Tn1oKB1oRYftN44TcCqg6yJ5o6RDV3tdNkTST3g0JM7PCf7yMGpawmhkfidAgMBAAECggEAePNv8kGF6/s1Me5oMYjS98pFWtC5t0fMLWf9GhouGppCPvJJWasJKYUbPFok2ucmZ55p1ueCX53OG6idUtmvBbawJeqVBi1CEEAV6eOqabtqPzZ/UNtZz/8cA2qdzo0osi2Y+gamKIwZNy8VOwkr7NX1qgkhqUZAw6rm9V7E0j4TMklaj62HAvVDQHAqS/i95i7uAOypcKYKTEW8fgQzQIIUcJXz77EeBpd+FD+D3I+dUwOlAM4jZVRizQ817uT4xcEwM9rISj66U8ckry6O3yHBFGUHvRFHPjRCFHjo99xxh+TX7TLHEP7cyLsVs9QluvtRT77WC1BJKrFe37WKYQKBgQDscojSIQ7anqQuZDNGwi+trttftHOjFRTvtNHvHev+hdTc9okqUJ1qLBr/Hi4fLtwC7tWOdAfpo4138p7GqPK2sLI90iJHzeud6g7v1Nep5UunIhw0pG1W1btivguXwmZWxg//AbeENgeTWnp5nVIKg69EOD8HzQKOipYqwC4ZWQKBgQC38YcbCjRJAKdMMIYE23plrSuA+UeGgNnAH/6u4rjaX67ViyIQetGFl1G2+pxY+JX4Gxf9in6AyCgDM7zBudNeo+vNlmD0F60cPKvdN4GLa4ZhcAEE9Ft0gz3/wSNShDS3Okql7rUJ82X6HUv4kP07rPgRXz5ubgv5LIYU8oos5QKBgFb2qSvfzL7LqkcFw3Y65Z5TuYuxqvZIQrMHJk90CeBLCMdcRYayr7LlFejPCNefPHw3q4QBWQeth9KeAZe/e8WFp4jlSIZwTkB+XZAIEGoEU5bdDNKvxycw5QKkM09VyJ4RqsB3uS9/T7n6hSFFo28Kj+PfbA9Kb65Nen2uwUXBAoGAdZWaDjj5r2kRiOzD3zsGBRUr6ChyBpM7n0tUnSsli8L6mlt0jSBV65YNkHCnnWBbTdzT+ymbMGvC2DksgpJtgeQ7ipKEq8SuzowiOSmQUDWOE59rp2UlSVfUPDeUgt0sbax/p47uf8vt9nLf66eTmaA6e7Bs0l7anJBPrfhVR9kCgYAlEXsdMTe9JrpjhdvYxHvto6OWyMPa36QGHMulXb6NYHfYDqvG8a4zJnTWmfP9/XwyyCL0GJN1VGCZKlnEQH6I+7P4jx/RsqRryQxs/Es8UiiKWTKcr9zeoVHKJXq6E6++TsVARG9g9RKJj0turAnUUmZU1CeZQzZ6xTYJkE02nw==",
                "json",
                "utf-8",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwkz5NNzXt9IcV8sheHaYskJ7G1kOEAyR5cn5CUw0iVqHURqokImbvNAILwMqRy4forXJNKM4CcjbuhigTmMxx6CYnn6n1yE8BHW1VGeFoJP9zFMtwTZboHHLteHCmWD0QDjj9yqhGxuM9Il6vPX/gtcpe5fLDY6yvFr9vSlD3q60GCkOvOScpL1YKmdFU28A7tz6O4V/IUhZdwM4LOdZCCNpTKou75lFT1hUuTarMbl9nD40ntGv6FeY1QeDNEXMyTJa8yfUwjUOs/ixkvyfcVAa5GNLygKjg7IoZ3CSA06GIuZUF16cdatOfFGWclr/6Fx/x8ubaZzg9t6zgOCU3wIDAQAB",
                "RSA2");//SHA256
        AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
        request.setBizContent("{" +
                "\"bill_type\":\"trade\"," +
                "\"bill_date\":\"2019-03-27\"" +
                "  }");
        AlipayDataDataserviceBillDownloadurlQueryResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
            System.out.println(response.getBillDownloadUrl());
        } else {
            System.out.println("调用失败");
        }
    }
}
