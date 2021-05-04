package com.mi.receive.service.Impl;

import com.mi.receive.service.DirectReciveService;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author : Rong
 * @date : 2021/5/1
 * @Desc:
 */
@Service
@Slf4j
public class DirectReciveServiceImpl implements DirectReciveService{


//    @RabbitListener(
//            containerFactory = "rabbitListenerContainerFactory",
//            bindings = {
//                    @QueueBinding(
//                            value = @Queue(name = "direct.queue.test"),
//                            exchange = @Exchange(name = "dircet.exchange.test",
//                                    type = ExchangeTypes.DIRECT),
//                            key = "direct.queue.test"
//                    )
//            }
//    )
    @Override
    public void DirectRecive(@Payload Message message) {
        log.info("========direct接受消息===========");
        String messageBody = new String(message.getBody());
        log.info(" body = {} " ,messageBody);
    }

    /**
     * 使用Qos
     * @param message
     * @param channel
     */
    @RabbitListener(
            containerFactory = "rabbitListenerContainerFactory",
            bindings = {
                    @QueueBinding(
                            value = @Queue(name = "direct.queue.test"),
                            exchange = @Exchange(name = "dircet.exchange.test",
                                    type = ExchangeTypes.DIRECT),
                            key = "direct.queue.test"
                    )
            }
    )
    @Override
    public void QosDirectRecive(@Payload Message message, Channel channel) throws IOException {
        log.info("========directQos接受消息===========");
        channel.basicQos(0,10,false);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String message = new String(body, "UTF-8");
                log.info("[x] Received '" + message + "'");

                channel.basicAck(envelope.getDeliveryTag(), true);
            }
        };
        //. 设置 Channel 消费者绑定队列
        channel.basicConsume("direct.queue.test",false,consumer);
    }

    /**
     * 处理异常的没有签收的消息重回队列
     * @param message
     * @param channel
     */
//    @RabbitListener(
//            containerFactory = "rabbitListenerContainerFactory",
//            bindings = {
//                    @QueueBinding(
//                            value = @Queue(name = "direct.queue.test"),
//                            exchange = @Exchange(name = "dircet.exchange.test",
//                                    type = ExchangeTypes.DIRECT),
//                            key = "direct.queue.test"
//                    )
//            }
//    )
    @Override
    public void sendErroBackQueueMessage(Message message, Channel channel) throws IOException {
        log.info("========direct重回队列接受消息===========");

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //模拟msg
                String message = new String(body, "UTF-8");
                log.info("[x] Received '" + message + "'");
                try {
                    Thread.sleep(2000);

                    //手动ack false
//                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //重新放入队列
                // 三个参数 : DeliveryTag, 是否批量拒绝, 是否可以重回队列
                // boolean multiple 是否批量   , boolean requeue 是否重回队列
                log.info("header = {}",properties.getHeaders().get("header"));
                if (Integer.parseInt(properties.getHeaders().get("header").toString()) == 0){
                    // 重回队列
                    channel.basicNack(envelope.getDeliveryTag(), false, false);
                }else {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        channel.basicConsume("direct.queue.test",false,consumer);
    }


    /**
     * 接收队列过期消息
     * @param message
     * @param channel
     */
//    @RabbitListener(
//            containerFactory = "rabbitListenerContainerFactory",
//            bindings = {
//                    @QueueBinding(
//                            value = @Queue(name = "queueTimeOut.queue.test",durable = "true"),
//                            exchange = @Exchange(name = "queueTimeOut.exchange.test",
//                                    type = ExchangeTypes.DIRECT),
//                            key = "key.queuetime.out"
//                    )
//            }
//    )
//    @Override
//    public void queueTimeOutMessage(Message message, Channel channel) throws IOException {
//        log.info("========direct队列消息过期===========");
//        DefaultConsumer consumer = new DefaultConsumer(channel) {
//            @Override
//            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//
//                    String message = new String(body, "UTF-8");
//                    log.info("[x] Received '" + message + "'");
//                    //手动ack false
//                    channel.basicAck(envelope.getDeliveryTag(), false);
//                }
//        };
//        channel.basicConsume("queueTimeOut.queue.test",false,consumer);
//    }


    /**
     * 监听死信队列
     */
    @RabbitListener(
            containerFactory = "rabbitListenerContainerFactory",
            bindings = {
                    @QueueBinding(
                            value = @Queue(name = "dead.queue.test"),
                            exchange = @Exchange(name = "dead.exchange.test",
                                    type = ExchangeTypes.DIRECT),
                            key = "key.queue.dead"
                    )
            }
    )
    @Override
    public void deadQueueListenter(@Payload Message message, Channel channel) throws IOException {
        log.info("========direct死信队列,业务场景超时===========");
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String message = new String(body, "UTF-8");
                log.info("[x] Received '" + message + "'");
                // 手动 false ack
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        //. 设置 Channel 消费者绑定队列
        channel.basicConsume("dead.queue.test",false,consumer);
    }


    /**
     * 监听延迟队列
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(
            containerFactory = "rabbitListenerContainerFactory",
            bindings = {
                    @QueueBinding(
                            value = @Queue(name = "delayed.queue.test"),
                            exchange = @Exchange(name = "delayed.exchange.test",
                                    type = "x-delayed-message"),
                            key = "key.queue.delayed"
                    )
            }
    )
    @Override
    public void delayedQueueListenter(@Payload Message message, Channel channel) throws IOException {
        log.info("========direct延迟队列,业务场景订单 超时===========");
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String message = new String(body, "UTF-8");
                log.info("[x] Received '" + message + "'");
                // 手动 false ack
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        //. 设置 Channel 消费者绑定队列
        channel.basicConsume("delayed.queue.test",false,consumer);
    }


}