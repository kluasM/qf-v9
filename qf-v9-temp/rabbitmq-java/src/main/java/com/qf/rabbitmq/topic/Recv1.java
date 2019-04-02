package com.qf.rabbitmq.topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/18
 *
 */
public class Recv1 {

    //简单队列
    private static final String QUEUE_NAME = "topic_exchange_queue_1";
    //简单交换机
    private static final String EXCHANGE_NAME = "topic_exchange";

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
        //声明队列，并且将队列绑定到交换机上
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"nba.*");
        //4.声明消费者
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                //处理接收到的消息信息
                String msg = new String(body,"utf-8");
                System.out.println("Recv1--->接收到的消息为："+msg);
            }
        };
        //5.让消费者去监听队列
        //autoAck:为true即自动回复模式
        channel.basicConsume(QUEUE_NAME,true,consumer);
    }
}
