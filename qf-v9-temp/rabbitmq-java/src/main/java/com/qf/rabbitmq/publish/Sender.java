package com.qf.rabbitmq.publish;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/18
 * 默认是轮流发送给消费者
 * 希望处理快的就多处理点
 *
 */
public class Sender {

    //简单交换机
    private static final String EXCHANGE_NAME = "fanout_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        //1.连接MQ服务器
        ConnectionFactory factory = new ConnectionFactory();
        factory.setVirtualHost("/java1809");
        factory.setUsername("java1809");
        factory.setPassword("123");
        factory.setHost("192.168.77.137");
        factory.setPort(5672);
        //2.发送消息给MQ服务器
        Connection connection = factory.newConnection();
        //3.基于channel，类似会话的作用
        Channel channel = connection.createChannel();
        //4.声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //5.发布消息
        for (int i = 1; i <= 10; i++) {
            String msg = "第"+i+"次学习打卡";
            channel.basicPublish(EXCHANGE_NAME,"",null,msg.getBytes());
        }
        System.out.println("消息发送完毕！");
    }
}
