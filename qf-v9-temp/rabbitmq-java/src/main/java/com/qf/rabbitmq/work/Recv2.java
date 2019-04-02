package com.qf.rabbitmq.work;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/18
 * 限流+结合手工回复模式
 */
public class Recv2 {

    //简单队列
    private static final String QUEUE_NAME = "work_queue";

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
        final Channel channel = connection.createChannel();
        //设置一次只接收一个消息
        channel.basicQos(1);
        //4.声明消费者
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                //处理接收到的消息信息
                String msg = new String(body,"utf-8");
                System.out.println("Recv2--->接收到的消息为："+msg);
                //刚好处理得比较慢
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //这里手工回复，消息处理完毕
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        //5.让消费者去监听队列
        //autoAck:为true即自动回复模式---》修改手工模式
        //channel.basicConsume(QUEUE_NAME,true,consumer);
        channel.basicConsume(QUEUE_NAME,false,consumer);
    }
}
