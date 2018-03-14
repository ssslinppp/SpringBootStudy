package com.ssslinppp.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
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

    ////////////////////// 设置Queue 并绑定 start /////////////////////
    /// 在消息发送端设置Queue，并不是为了消费消息，而是为了保证发送到
    /// Exchange中的消息不会被丢失，可以路由到Queue中保存
    ///////////////////////////////////////////////////////////
    private static final String LOG_ROUTING_KEY_ERR = "*.err.#";
    private static final String LOG_ROUTING_KEY_WARN = "*.warn.#";
    private static final String LOG_ROUTING_KEY_INFO = "*.info.#";
    private static final String LOG_ROUTING_KEY_DEBUG = "*.debug.#";

    //// queue: 绑定error warn
    @Bean
    public Queue queueOfErrWarn() {
        return new Queue("practice.log.error.warn.queue");
    }

    @Bean
    public Binding bindingErr(TopicExchange topic, Queue queueOfErrWarn) {
        return BindingBuilder.bind(queueOfErrWarn).to(topic).with(LOG_ROUTING_KEY_ERR);
    }

    @Bean
    public Binding bindingWarn(TopicExchange topic, Queue queueOfErrWarn) {
        return BindingBuilder.bind(queueOfErrWarn).to(topic).with(LOG_ROUTING_KEY_WARN);
    }

    //// queue: 绑定info debug
    @Bean
    public Queue queueOfInfoDebug() {
        return new Queue("practice.log.Info.debug.queue");
    }

    @Bean
    public Binding bindingInfo(TopicExchange topic, Queue queueOfInfoDebug) {
        return BindingBuilder.bind(queueOfInfoDebug).to(topic).with(LOG_ROUTING_KEY_INFO);
    }

    @Bean
    public Binding bindingDebug(TopicExchange topic, Queue queueOfInfoDebug) {
        return BindingBuilder.bind(queueOfInfoDebug).to(topic).with(LOG_ROUTING_KEY_DEBUG);
    }

    ////////////////////// 设置Queue end /////////////////////


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
