package com.ssslinppp.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Desc:
 * <p>
 * User: liulin ,Date: 2018/3/7 , Time: 16:53 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class RabbitConfig {
    private static final String LOG_TOP_EXCHANGE_NAME = "practice.log.level.exchage";

    @Bean
    public TopicExchange topic() {
        return new TopicExchange(LOG_TOP_EXCHANGE_NAME);
    }

    //////////////////////////// 发送端：配置Json MessageConverter ////////////////////////////////////
    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    //////////////////////////// 发送端：配置Json MessageConverter (end)////////////////////////////////////
}
