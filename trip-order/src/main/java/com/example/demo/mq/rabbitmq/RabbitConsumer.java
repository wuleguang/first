package com.example.demo.mq.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author : wulg29230
 * @Date : 2020/3/12 14:14
 **/
@Component
public class RabbitConsumer {
    private static Logger logger = LoggerFactory.getLogger(RabbitConsumer.class);

    //注解很重要，标注了exchange、routingkey、queue之间的关系，对应指定到这个queue
  /*  @RabbitListener(queues = "exchange.light")
    public void receiver(Message message) {
        logger.info("sendPush接收消息:{}", new String(message.getBody()));
    }*/
}
