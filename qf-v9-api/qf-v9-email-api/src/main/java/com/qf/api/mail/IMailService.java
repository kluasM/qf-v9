package com.qf.api.mail;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/19
 */
public interface IMailService {

    public String sendSimpleMail(String to,String subject,String content);
}
