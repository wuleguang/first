package com.example.demo.mq.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * @author : wulg29230
 * @Date : 2020/3/12 10:22
 **/
@Configuration
//@EnableRabbit
@Slf4j
public class RabbitConfiguration {
    @Value("${spring.rabbitmq.host}")
    private String rabbitMqHost;

    @Value("${spring.rabbitmq.port}")
    private Integer rabbitMqPort;

    @Value("${spring.rabbitmq.username}")
    private String rabbitUserName;

    @Value("${spring.rabbitmq.password}")
    private String rabbitPassword;

    @Value("${spring.rabbitmq.virtual-host}")
    private String vHost;

    @Bean
    public CachingConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory= new CachingConnectionFactory();
        connectionFactory.setHost(rabbitMqHost);
        connectionFactory.setPort(rabbitMqPort);
        connectionFactory.setPassword(rabbitPassword);
        connectionFactory.setUsername(rabbitUserName);
        connectionFactory.setVirtualHost(vHost);
        //60s超时
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);
        //设置每个Connection中缓存Channel的数量，不是最大的。操作rabbitmq之前（send/receive message等）
        // 要先获取到一个Channel.获取Channel时会先从缓存中找闲置的Channel，如果没有则创建新的Channel，
        // 当Channel数量大于缓存数量时，多出来没法放进缓存的会被关闭。
        connectionFactory.setChannelCacheSize(10);
        //单位：毫秒；配合channelCacheSize不仅是缓存数量，而且是最大的数量。
        // 从缓存获取不到可用的Channel时，不会创建新的Channel，会等待这个值设置的毫秒数
        //同时，在CONNECTION模式，这个值也会影响获取Connection的等待时间，
        // 超时获取不到Connection也会抛出AmqpTimeoutException异常。
        connectionFactory.setChannelCheckoutTimeout(600);
        //仅在CONNECTION模式使用，设置Connection的缓存数量。
        connectionFactory.setConnectionCacheSize(25);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        //客户端开启confirm模式
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        //确认消息是否到达broker服务器，也就是只确认是否正确到达exchange中即可，
        //只要正确的到达exchange中，broker即可确认该消息返回给客户端ack。
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if(ack){
                    log.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
                }else{
                    log.info("消息发送失败:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
                }
            }
        });
        //confrim 模式只能保证消息到达 broker，不能保证消息准确投递到目标 queue 里。
        // 在有些业务场景下，我们需要保证消息一定要投递到目标 queue 里，此时就需要用到 return 退回模式。
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                //重新发布
                System.out.println("Returned Message："+replyText);
            }
        });
        RetryTemplate retryTemplate = new RetryTemplate();
        //重试的回退策略，指以何种方式进行下一次重试（第一次重试后什么时候进行第二次重试），比如过了15秒后重试，随机时间重试。
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        //初始间隔
        backOffPolicy.setInitialInterval(500);
        //递增倍数（即下次间隔是上次的多少倍）
        backOffPolicy.setMultiplier(10.0);
        //最大间隔
        backOffPolicy.setMaxInterval(10000);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        //重试策略或条件，可以指定超时重试，一直重试，简单重试等。
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        //重试3次
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);
        rabbitTemplate.setRetryTemplate(retryTemplate);
        return rabbitTemplate;
    }


    //rabbit.declareBinding(BindingBuilder.
    // bind(new Queue("log.debug")).to(new HeadersExchange("log.headers.exchange")).whereAll(headerValues).match());

    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        return factory;
    }

}
