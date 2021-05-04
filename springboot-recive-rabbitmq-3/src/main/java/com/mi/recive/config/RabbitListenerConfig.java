package com.mi.recive.config;

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
    private FanoutListenerAdapter fanoutListenerAdapter;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("42.194.178.240");
        connectionFactory.setPort(5672);
        connectionFactory.setPassword("guest");
        connectionFactory.setUsername("guest");
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
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


    /*********************************/
//    自定义MessageListenerAdapter
    /***声明 fanout 队列  一对多***/
//    @Bean
//    public Exchange fanoutExchange02(){
//        return new FanoutExchange("fanout.exchange01.test");
//    }
//
//    @Bean
//    public Queue fanoutQueue02(){
//        return new Queue("fanout.queue02.test");
//    }
//
//    @Bean
//    public Binding fanoutBinding02(){
//        return new Binding("fanout.queue02.test",
//                Binding.DestinationType.QUEUE,
//                "fanout.exchange01.test",
//                "",null);
//    }



    @Bean //connectionFactory 也是要和最上面方法名保持一致
    public SimpleMessageListenerContainer messageListenerContainer(@Autowired ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer container =
                new SimpleMessageListenerContainer(connectionFactory);
        // 接受这个消息
        container.setQueueNames("fanout.queue01.test");
        container.setConcurrentConsumers(1);  // 当前的消费者数量
        container.setMaxConcurrentConsumers(10);  // 最大的消费者数量
        container.setDefaultRequeueRejected(false); // 是否重回队列
        container.setAcknowledgeMode(AcknowledgeMode.AUTO); // 签收模式
        container.setExposeListenerChannel(true);
        container.setConsumerTagStrategy(queue -> queue + "_" + UUID.randomUUID().toString());
        // 第二种
        // 多个channel 对应一个
        container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            log.info("message:{} ",message);
            channel.basicAck(
                    message.getMessageProperties().getDeliveryTag(),
                    false
            );
        });
//        prefetch_count参数仅仅在basic.consume的autoAck参数设置为false的前提下才生效，
// 也就是不能使用自动确认，自动确认的消息没有办法限流。
        container.setPrefetchCount(10);  // 限流最大


        // 设置自定义MessageListenerAdapter自定义消息监听
        // 1. 实现handleMessage方法
        // 2. 高阶模式：自定义：“队列名--》》方法名”
        MessageListenerAdapter messageListenerAdapter =
                new MessageListenerAdapter(fanoutListenerAdapter);
        // 添加默认方法
//        messageListenerAdapter.setDefaultListenerMethod("consumerMessage");

        // HashMap
        Map<String,String> methodMap
                =new HashMap<>(8);
        // 自定义：“队列名--》》方法名”
        methodMap.put("direct.queue01.test","directListenerMethod1");
        methodMap.put("fanout.queue01.test","fanoutListenerMethod2");
        messageListenerAdapter.setQueueOrTagToMethodName(methodMap);

//        // 自定义MessgaeConverter
//        Jackson2JsonMessageConverter messageConverter =
//                new Jackson2JsonMessageConverter();
//        // 转为JSON
//        messageConverter.setClassMapper(new ClassMapper() {
//            @Override
//            public void fromClass(Class<?> aClass, MessageProperties messageProperties) {
//
//            }
//
//            @Override
//            public Class<?> toClass(MessageProperties messageProperties) {
//                return null;
//            }
//        });
//
//        messageListenerAdapter.setMessageConverter(messageConverter);

        container.setMessageListener(messageListenerAdapter);
        return container;
    }

}