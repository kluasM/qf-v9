package com.qf.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/21
 */
public class MyMessageListener implements MessageListener {

    public void onMessage(Message message, byte[] pattern) {
        //1.获取到channel信息
        String channel = new String(message.getChannel());
        //2.获取到消息信息
        String info = new String(message.getBody());
        //System.out.println("来自："+channel+",发来的消息：");
        System.out.println(info);
    }
}
