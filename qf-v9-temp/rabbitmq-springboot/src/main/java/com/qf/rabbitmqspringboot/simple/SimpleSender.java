package com.qf.rabbitmqspringboot.simple;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/18
 */
@Component
public class SimpleSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String msg){
        rabbitTemplate.convertAndSend("","springboot-simple-queue",
                msg);
        System.out.println("发送消息成功！");
    }
}
