package com.example.demo.mq.rabbitmq;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author : wulg29230
 * @Date : 2020/3/13 11:14
 **/
@Component
public class RabbitCreate {

    @Value(value = "exchange.light")
    private String exchangeName;
    @Value(value = "exchange.light")
    private String queueName;
    @Value(value = "routingKey.light")
    private String routingKey;
    @Resource
    private ConnectionFactory connectionFactory;
    @PostConstruct
    public void createExchangeAndQueue(){
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        TopicExchange topicExchange = new TopicExchange(exchangeName);
        rabbitAdmin.declareExchange(topicExchange);
        Queue queue = new Queue(queueName);
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(topicExchange).with(routingKey));
    }
}
