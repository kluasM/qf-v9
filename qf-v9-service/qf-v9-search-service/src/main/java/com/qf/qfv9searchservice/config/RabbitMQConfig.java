package com.qf.qfv9searchservice.config;

import com.qf.v9.common.constant.RabbitMQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/18
 */
@Configuration
public class RabbitMQConfig {

    //1.声明队列
    @Bean
    public Queue getQueue(){
        return new Queue(RabbitMQConstant.BACKGROUND_PRODUCT_SAVE_UPDATE_QUEUE);
    }
    //2.声明交换机
    @Bean
    public TopicExchange getTopicExchange(){
        return new TopicExchange(RabbitMQConstant.BACKGROUND_EXCHANGE);
    }
    //3.建立绑定关系
    @Bean
    public Binding getBinding(Queue getQueue,TopicExchange getTopicExchange){
        return BindingBuilder.bind(getQueue).to(getTopicExchange).with("product.add");
    }
}
