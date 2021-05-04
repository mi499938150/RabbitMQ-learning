package com.mi.send.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : Rong
 * @date : 2021/4/30
 * @Desc:
 */
@Component
@Slf4j
public class RabbitListenerConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("42.194.178.240");
        connectionFactory.setPort(5672);
        connectionFactory.setPassword("guest");
        connectionFactory.setUsername("guest");
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.SIMPLE);
        connectionFactory.setPublisherReturns(true);
        connectionFactory.createConnection();
        return connectionFactory;
    }

    @Bean
    public RabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }
    @Bean
    @Qualifier("rabbitTemplate")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //开启mandatory模式（开启失败回调）
        rabbitTemplate.setMandatory(true);
        //添加失败回调方法
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.info("message:{}, replyCode:{}, replyText:{}, exchange:{}, routingKey:{}",
                    message, replyCode, replyText, exchange, routingKey);
        });
        // 添加发送方确认模式方法
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) ->
                log.info("correlationData:{}, ack:{}, cause:{}",
                        correlationData.getId(), ack, cause));
        return rabbitTemplate;
    }


    /***声明 direct 队列  一对一***/
    @Bean
    public Exchange directExchange(){
        return new DirectExchange("dircet.exchange.test");
    }

    @Bean
    public Queue directQueue(){
        return new Queue("direct.queue.test");
    }

    @Bean
    public Binding directBinding(){
        return new Binding("direct.queue.test",
                                Binding.DestinationType.QUEUE,
                                "dircet.exchange.test",
                                    "direct.key",null);
    }

    /**声明 fanout   一对多队列**/

    @Bean
    public Exchange fanoutExchange(){
        return new FanoutExchange("fanout.exchange.test");
    }

    @Bean
    public Queue fanoutQueue1(){
        return new Queue("fanout.queue.test1");
    }

    @Bean
    public Queue fanoutQueue2(){
        return new Queue("fanout.queue.test2");
    }

    @Bean
    public Queue fanoutQueue3(){
        return new Queue("fanout.queue.test3");
    }


    @Bean
    public Binding fanoutBinding1(){
        return new Binding("fanout.queue.test1",
                Binding.DestinationType.QUEUE,
                "fanout.exchange.test",
                "",null);
    }


    @Bean
    public Binding fanoutBinding2(){
        return new Binding("fanout.queue.test2",
                Binding.DestinationType.QUEUE,
                "fanout.exchange.test",
                "",null);
    }


    @Bean
    public Binding fanoutBinding3(){
        return new Binding("fanout.queue.test3",
                Binding.DestinationType.QUEUE,
                "fanout.exchange.test",
                "",null);
    }


    /** 声明Topic 主题 交换机**/


    @Bean
    public Exchange topicExchange(){
        return new TopicExchange("topic.exchange.test");
    }

    @Bean
    public Queue topicQueue1(){
        return new Queue("topic.queue.test");
    }

    @Bean
    public Binding topicBinding() {
        return new Binding("topic.queue.test",
                Binding.DestinationType.QUEUE,
                "topic.exchange.test",
                "key.order.rabbit", null);
    }


    /** direct 交换机 **/
    @Bean
    public Exchange directTopicExchange(){
        return new DirectExchange("directTopic.exchange.test");
    }

    @Bean
    public Queue directTopicQueue(){
        return new Queue("directTopic.queue.test");
    }

    @Bean
    public Binding directTopicBinding() {
        return new Binding("directTopic.queue.test",
                Binding.DestinationType.QUEUE,
                "directTopic.exchange.test",
                "key.order.rabbit", null);
    }

    /**设置单个消息过期时间**/
    /* 发送消息时针对单个消息设置过期时间*/

    @Bean
    public Exchange timeOutExchange(){
        return new DirectExchange("timeOut.exchange.test");
    }

    @Bean
    public Queue timeOutQueue(){
        return new Queue("timeOut.queue.test");
    }

    @Bean
    public Binding timeOutBinding(){
        return new Binding("timeOut.queue.test",
                Binding.DestinationType.QUEUE,
                "timeOut.exchange.test",
                "key.time.out",null);
    }

    /* 设置队列过期时间*/
    @Bean
    public Exchange queueTimeOutExchange(){
        return new DirectExchange("queueTimeOut.exchange.test",true,false);
    }

    // 声明过期的队列并定义队列名称
    @Bean
    public Queue queueTimeOutQueue(){
        // 消息过期 特殊的args
        Map<String,Object> args  = new HashMap<>(16);
        args.put("x-message-ttl",100000);
        // 绑定死信交换机名称
        args.put("x-dead-letter-exchange", "dead.exchange.test");
        // 置发送给死信交换机的routingKey
        args.put("x-dead-letter-routing-key", "key.queue.dead");
        // 设置队列可以存储的最大消息数量
        args.put("x-max-length", 10);
        return new Queue("queueTimeOut.queue.test"
                ,true,false,false,args);
    }

    @Bean
    public Binding queueTimeOutBinding(){
        return new Binding("queueTimeOut.queue.test",
                Binding.DestinationType.QUEUE,
                "queueTimeOut.exchange.test",
                "key.queuetime.out",null);
    }

    /* 死信队列*/
    @Bean
    public Exchange deadExchange(){
        return new DirectExchange("dead.exchange.test",true,false);
    }

    // 声明过期的队列并定义队列名称
    @Bean
    public Queue deadQueue(){

        return new Queue("dead.queue.test"
                ,true,false,false);
    }

    @Bean
    public Binding deadBinding(){
        return new Binding("dead.queue.test",
                Binding.DestinationType.QUEUE,
                "dead.exchange.test",
                "key.queue.dead",null);
    }

    /** 延迟队列*/
    @Bean
    public Exchange CustomExchange(){
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange("delayed.exchange.test","x-delayed-message",true,false,args);
    }

    // 声明过期的队列并定义队列名称
    @Bean
    public Queue delayQueue(){

        return new Queue("delayed.queue.test"
                ,true,false,false);
    }

    @Bean
    public Binding delayBinding(){
        return new Binding("delayed.queue.test",
                Binding.DestinationType.QUEUE,
                "delayed.exchange.test",
                "key.queue.delayed",null);
    }



    /******自定义监听器**********/






}