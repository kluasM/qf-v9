package com.qf.qfv9mailservice.service;

import com.qf.api.mail.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/19
 */
@Component
public class MailServiceImpl implements IMailService{

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.address}")
    private String from;

    @Override
    public String sendSimpleMail(String to, String subject, String content) {
        //1.性能角度
        //基础性服务，很多系统都会找它来发送邮件
        //1000,采用线程池的方式来改造这块代码
        //变成一个多线程的处理---利用好服务器的资源 4核 64核
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
        return "success";

        //2.可靠性角度
        //发送失败
        //网络不稳定，导致这次发送失败
        //补发，记录下发送失败的邮件记录，然后后期定期扫描记录，重新发送
        //重新发送3次失败，不发送了，记录下记录，人工去核定


    }
}
