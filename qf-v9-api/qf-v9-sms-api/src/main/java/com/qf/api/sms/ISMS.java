package com.qf.api.sms;

import com.qf.api.sms.pojo.SMSResponse;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/19
 */
public interface ISMS {

    //返回的信息，封装成一个对象
    public SMSResponse sendCode(String to, String code);
}
