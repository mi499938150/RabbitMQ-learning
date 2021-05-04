package com.mi.recive.config;

import com.mi.recive.service.impl.DirectListenerAdapter;
import com.mi.recive.service.impl.FanoutListenerAdapter;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author : Rong
 * @date : 2021/4/30
 * @Desc:
 */
@Component
@Slf4j
public class RabbitListenerConfig {

    @Autowired
    private DirectListenerAdapter directListenerAdapter;

    /* 自定义MessageListenerAdapter */
    @Autowired
    private FanoutListenerAdapter fanoutListenerAdapter;

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
    public RabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }


    /*********************************/
//    自定义MessageListenerAdapter

    /***声明 direct 队列  一对一***/
    @Bean
    public Exchange directExchange01() {
        return new DirectExchange("dircet.exchange01.test");
    }

    @Bean
    public Queue directQueue01() {
        return new Queue("direct.queue01.test");
    }

    @Bean
    public Binding directBinding01() {
        return new Binding("direct.queue01.test",
                Binding.DestinationType.QUEUE,
                "dircet.exchange01.test",
                "direct.queue01.key", null);
    }

    /***声明 fanout 队列  一对多***/
    @Bean
    public Exchange fanoutExchange01(){
        return new FanoutExchange("fanout.exchange01.test");
    }

    @Bean
    public Queue fanoutQueue01(){
        return new Queue("fanout.queue01.test");
    }

    @Bean
    public Binding fanoutBinding01(){
        return new Binding("fanout.queue01.test",
                Binding.DestinationType.QUEUE,
                "fanout.exchange01.test",
                "",null);
    }


    /**
     * 配置简单 SimpleMessageListenerContainer
     */

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(@Autowired ConnectionFactory connectionFactory) {

        SimpleMessageListenerContainer container =
                new SimpleMessageListenerContainer(connectionFactory);

        container.setQueueNames("fanout.queue01.test"); // 监听队列
        container.setConcurrentConsumers(1);  // 当前的消费者数量
        container.setMaxConcurrentConsumers(10);  // 最大的消费者数量
        container.setDefaultRequeueRejected(false); // 是否重回队列
        container.setAcknowledgeMode(AcknowledgeMode.AUTO); // 签收模式
        container.setExposeListenerChannel(true);
        container.setConsumerTagStrategy(queue -> queue + "_" + UUID.randomUUID().toString());
        // 监听多个channel
        container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            log.info("message:{} ", message);
            channel.basicAck(
                    message.getMessageProperties().getDeliveryTag(),
                    false
            );
        });
          // prefetch_count参数仅仅在basic.consume的autoAck参数设置为false的前提下才生效，
        // 也就是不能使用自动确认，自动确认的消息没有办法限流。
        container.setPrefetchCount(10);  // 限流最大


        /************* 设置自定义MessageListenerAdapter自定义消息监听   ************/
        // 1. 实现handleMessage方法
        // 2. 高阶模式：自定义：“队列名--》》方法名”
        MessageListenerAdapter messageListenerAdapter =
                new MessageListenerAdapter(fanoutListenerAdapter);
        // 修改默认方法
        // messageListenerAdapter.setDefaultListenerMethod("consumerMessage");
        // HashMap
        Map<String, String> methodMap
                = new HashMap<>(8);
        // 自定义：“队列名--》》方法名”
        methodMap.put("fanout.queue01.test", "fanoutListenerMethod1");
        messageListenerAdapter.setQueueOrTagToMethodName(methodMap);
        container.setMessageListener(messageListenerAdapter);
        /************* 设置自定义MessageListenerAdapter自定义消息监听   ************/
        return container;
    }
}