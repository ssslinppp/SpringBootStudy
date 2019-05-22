package com.ssslinppp.config;

import com.ssslinppp.confirm.CompleteMessageCorrelationData;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.CorrelationDataPostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
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
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());  // Json消息转换器

        ////////////////////  publisher confirm ,start //////////////////////////////////////
        //publisher Confirms callback ：当broker应答ack或nack时，会回调该方法
        rabbitTemplate.setConfirmCallback((correlation, ack, reason) -> {
            // TODO 如果发送消息时，指定了msg唯一ID（delivery_tag）,则这里可以通过这个ID来区分msg

            if (ack) {
                System.out.println("publisher confirm [ack] for correlation: " + correlation);
            } else {
                //Message确认失败
                System.out.println("publisher confirm [Nack] ,reason: " + reason + ", for correlation: " + correlation);
            }
        });

        // 该处理器会在Message发送之前调用，用于更新或替换CorrelationData
        rabbitTemplate.setCorrelationDataPostProcessor(new CorrelationDataPostProcessor() {
            @Override
            public CorrelationData postProcess(Message message, CorrelationData correlationData) {
                //TODO 在这里可以为Message分配一个全局唯一的ID（delivery_tag），用于区分消息

                // 只是简单的将发送的消息封装起来
                return new CompleteMessageCorrelationData(correlationData != null ? correlationData.getId() : null, message);
            }
        });
        ////////////////////  publisher confirm , end //////////////////////////////////////


        //////////////////////////////// 设置 mandatory 属性 //////////////////////////////////////
        ////// 当 Message发送到Exchange后，若没有任何Queue绑定该Exchange，则Message会返回给publisher /////////////
        ////// 通过设置 returnCallback，publisher可以接收broker返回的Message ////////////////////////////////////
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            System.out.println("-------mandatory(true): exchange route msg to queue fail ------------");
            System.out.println("Returned: " + message + "\nreplyCode: " + replyCode
                    + "\nreplyText: " + replyText + "\nexchange/rk: " + exchange + "/" + routingKey);
            System.out.println("----------------------------------------------");

            // TODO： 可以尝试处理该消息，比如：重新发送、记录到日志等

        });
        /////////////////////////////// 设置 mandatory 属性 end //////////////////////////////////////

        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    //////////////////////////// 发送端：配置Json MessageConverter (end)////////////////////////////////////
}
