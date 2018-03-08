package com.ssslinppp.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

/**
 * Desc: 主要完成 Queue到Exchange的绑定工作
 * <p>
 * User: liulin ,Date: 2018/3/7 , Time: 17:41 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class RabbitConfig implements RabbitListenerConfigurer {
    private static final String LOG_TOP_EXCHANGE_NAME = "practice.log.level.exchage";

    private static final String LOG_ROUTING_KEY_ERR = "*.err.#";
    private static final String LOG_ROUTING_KEY_WARN = "*.warn.#";
    private static final String LOG_ROUTING_KEY_INFO = "*.info.#";
    private static final String LOG_ROUTING_KEY_DEBUG = "*.debug.#";

    @Bean
    public TopicExchange topic() {
        return new TopicExchange(LOG_TOP_EXCHANGE_NAME);
    }

    ///////////////////////  绑定 err/warn  //////////////////////////////////////////////
    @Bean
    public Queue queueOfErrWarn() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding bindingErr(TopicExchange topic, Queue queueOfErrWarn) {
        return BindingBuilder.bind(queueOfErrWarn).to(topic).with(LOG_ROUTING_KEY_ERR);
    }

    @Bean
    public Binding bindingWarn(TopicExchange topic, Queue queueOfErrWarn) {
        return BindingBuilder.bind(queueOfErrWarn).to(topic).with(LOG_ROUTING_KEY_WARN);
    }

    ///////////////////////  绑定 info/debug  //////////////////////////////////////////////
    @Bean
    public Queue queueOfInfoDebug() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding bindingInfo(TopicExchange topic, Queue queueOfInfoDebug) {
        return BindingBuilder.bind(queueOfInfoDebug).to(topic).with(LOG_ROUTING_KEY_INFO);
    }

    @Bean
    public Binding bindingDebug(TopicExchange topic, Queue queueOfInfoDebug) {
        return BindingBuilder.bind(queueOfInfoDebug).to(topic).with(LOG_ROUTING_KEY_DEBUG);
    }

    ///////////////////// 消费端：Json MessageConverter //////////////////////////////////////////////////
    @Bean
    public MappingJackson2MessageConverter consumerJsonMessageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(consumerJsonMessageConverter());
        return factory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }
    ///////////////////// 消费端：Json MessageConverter （end）//////////////////////////////////////////////////

}
