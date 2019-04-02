package com.qf.qfv9smsservice.service;

import com.google.gson.Gson;
import com.qf.api.sms.ISMS;
import com.qf.api.sms.pojo.SMSResponse;
import com.qf.qfv9smsservice.util.Config;
import com.qf.qfv9smsservice.util.HttpUtil;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/19
 */
@Component
public class SMSService implements ISMS {

    @Override
    public SMSResponse sendCode(String to, String code) {
        String tmpSmsContent = null;
        StringBuilder stringBuilder = new StringBuilder("【梦想科技】您的短信验证码为");
        stringBuilder.append(code);
        stringBuilder.append(",请在5分钟内使用。");
        try {
            //编码格式:UTF-8
            tmpSmsContent = URLEncoder.encode(stringBuilder.toString(), "UTF-8");
        } catch (Exception e) {
            //一定把异常打印出来
            e.printStackTrace();
        }
        String url = Config.BASE_URL;
        String body = "accountSid=" + Config.ACCOUNT_SID + "&to=" + to + "&smsContent=" + tmpSmsContent
                + HttpUtil.createCommonParam();
        // 提交请求
        String result = HttpUtil.post(url, body);
        //result(json)--->对象
        System.out.println("result:" + System.lineSeparator() + result);
        Gson gson = new Gson();
        return gson.fromJson(result, SMSResponse.class);
    }
}
