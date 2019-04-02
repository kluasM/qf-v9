package com.qf.rabbitmqspringboot.simple;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/18
 */
@Component
public class SimpleRecv {

    @RabbitListener(queues = "springboot-simple-queue")
    public void process(String msg){
        System.out.println("接收到的消息为："+msg);
    }
}
