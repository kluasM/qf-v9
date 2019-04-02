package com.qf.qfv9background.config;

import com.qf.v9.common.constant.RabbitMQConstant;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/18
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange getTopicExchange(){
        return new TopicExchange(RabbitMQConstant.BACKGROUND_EXCHANGE);
    }
}
