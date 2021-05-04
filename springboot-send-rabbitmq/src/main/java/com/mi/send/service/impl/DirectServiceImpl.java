package com.mi.send.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mi.send.dto.OrderMessageDTO;
import com.mi.send.service.DirectService;
import com.rabbitmq.client.AMQP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Rong
 * @date : 2021/5/1
 * @Desc:
 */
@Slf4j
@Service
public class DirectServiceImpl implements DirectService {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void sendMessage() {
        log.info("==========发送Direct类型消息=======");
        try {
            String directStr = "Hello,我是directMesage";
            // 第一种方式
            OrderMessageDTO orderMessageDTO = new OrderMessageDTO();
            orderMessageDTO.setProductId(100);
            orderMessageDTO.setPrice(new BigDecimal("20"));
            orderMessageDTO.setOrderId(1);
            String messageToSend = objectMapper.writeValueAsString(orderMessageDTO);
            // 发送端确认是否确认消费
            CorrelationData correlationData = new CorrelationData();
            // 唯一ID
            correlationData.setId(orderMessageDTO.getOrderId().toString());
            // 发送
            rabbitTemplate.convertAndSend("dircet.exchange.test","direct.queue.test",messageToSend,correlationData);
            log.info("发送成功");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送Qos 限流消息
     */
    @Override
    public void sendQosMessage() throws JsonProcessingException {
        /* 使用MessageProperties传递的对象转换成message*/
        MessageProperties messageProperties = new MessageProperties();
        OrderMessageDTO orderMessageDTO = new OrderMessageDTO();
        orderMessageDTO.setProductId(100);
        orderMessageDTO.setPrice(new BigDecimal("20"));
        orderMessageDTO.setOrderId(1);
        String messageToSend = objectMapper.writeValueAsString(orderMessageDTO);
        Message message = new Message(messageToSend.getBytes(),messageProperties);
        // 发送端确认是否确认消费
        CorrelationData correlationData = new CorrelationData();
        // 唯一ID
        correlationData.setId(orderMessageDTO.getOrderId().toString());
        rabbitTemplate.convertAndSend("dircet.exchange.test","direct.queue.test",message,correlationData);
    }

    /**
     * 重回队列
     */
    @Override
    public void requeueMessage(int i) throws JsonProcessingException {
        /* 使用MessageProperties传递的对象转换成message*/
        MessageProperties messageProperties = new MessageProperties();
        OrderMessageDTO orderMessageDTO = new OrderMessageDTO();
        orderMessageDTO.setProductId(100);
        orderMessageDTO.setPrice(new BigDecimal("20"));
        orderMessageDTO.setOrderId(i);
        String messageToSend = objectMapper.writeValueAsString(orderMessageDTO);
        Message message = new Message(messageToSend.getBytes(),messageProperties);
        // 发送端确认是否确认消费
        CorrelationData correlationData = new CorrelationData();
        // 唯一ID
        correlationData.setId(orderMessageDTO.getOrderId().toString());
        rabbitTemplate.convertAndSend("dircet.exchange.test","direct.queue.test",message,message1 -> {
            message1.getMessageProperties().setHeader("header",i);
            return message1;
        },correlationData);
    }


    /**
     * 发送消息到mq并设置过期时间  设置单个消息过期时间
     * @throws JsonProcessingException
     */
    @Override
    public void timeOutMessage() throws JsonProcessingException {
        /* 使用MessageProperties传递的对象转换成message*/
        MessageProperties messageProperties = new MessageProperties();
        OrderMessageDTO orderMessageDTO = new OrderMessageDTO();
        orderMessageDTO.setProductId(100);
        orderMessageDTO.setPrice(new BigDecimal("20"));
        orderMessageDTO.setOrderId(1);
        String messageToSend = objectMapper.writeValueAsString(orderMessageDTO);
        Message message = new Message(messageToSend.getBytes(),messageProperties);
        // 发送端确认是否确认消费
        CorrelationData correlationData = new CorrelationData();
        // 唯一ID
        correlationData.setId(orderMessageDTO.getOrderId().toString());
        // 另外的写法
//        AMQP.BasicProperties properties =
//                new AMQP.BasicProperties()
//                        .builder()
//                        .deliveryMode(2)
//                        .expiration("15000")
//                        .build();
        rabbitTemplate.convertAndSend("timeOut.exchange.test","key.time.out",messageProperties,
            // 设置消息过期时间： 单位：毫秒
                message1 -> {
                message1.getMessageProperties().setExpiration("10000");// 过期时间
                message1.getMessageProperties().setDeliveryMode(MessageDeliveryMode.fromInt(2)); // 持久化
            // 返回消息对象
                return message1;
                },correlationData);
    }


    /**
     * 队列过期时间信息
     */
    @Override
    public void queueTimeOutMessage() throws JsonProcessingException {
        /* 使用MessageProperties传递的对象转换成message*/
        MessageProperties messageProperties = new MessageProperties();
        OrderMessageDTO orderMessageDTO = new OrderMessageDTO();
        orderMessageDTO.setProductId(100);
        orderMessageDTO.setPrice(new BigDecimal("20"));
        orderMessageDTO.setOrderId(1);
        String messageToSend = objectMapper.writeValueAsString(orderMessageDTO);
        Message message = new Message(messageToSend.getBytes(),messageProperties);
        // 发送端确认是否确认消费
        CorrelationData correlationData = new CorrelationData();
        // 唯一ID
        correlationData.setId(orderMessageDTO.getOrderId().toString());
        rabbitTemplate.convertAndSend("queueTimeOut.exchange.test","key.queuetime.out",message,correlationData);
    }

    /**
     * 測試死信隊列
     */
    @Override
    public void deadMessag() throws JsonProcessingException {
        /* 使用MessageProperties传递的对象转换成message*/
        MessageProperties messageProperties = new MessageProperties();
        OrderMessageDTO orderMessageDTO = new OrderMessageDTO();
        orderMessageDTO.setProductId(100);
        orderMessageDTO.setPrice(new BigDecimal("20"));
        orderMessageDTO.setOrderId(1);
        String messageToSend = objectMapper.writeValueAsString(orderMessageDTO);
        Message message = new Message(messageToSend.getBytes(),messageProperties);
        // 发送端确认是否确认消费
        CorrelationData correlationData = new CorrelationData();
        // 唯一ID
        correlationData.setId(orderMessageDTO.getOrderId().toString());
        rabbitTemplate.convertAndSend("dead.exchange.test","key.queue.dead",message,correlationData);
    }

    /**
     * 延迟队列
     */
    @Override
    public void delayedMessage() throws JsonProcessingException {
        /* 使用MessageProperties传递的对象转换成message*/
        MessageProperties messageProperties = new MessageProperties();
        OrderMessageDTO orderMessageDTO = new OrderMessageDTO();
        orderMessageDTO.setProductId(100);
        orderMessageDTO.setPrice(new BigDecimal("20"));
        orderMessageDTO.setOrderId(1);
        String messageToSend = objectMapper.writeValueAsString(orderMessageDTO);
        Message message = new Message(messageToSend.getBytes(),messageProperties);
        // 发送端确认是否确认消费
        CorrelationData correlationData = new CorrelationData();
        // 唯一ID
        correlationData.setId(orderMessageDTO.getOrderId().toString());
        rabbitTemplate.convertAndSend("delayed.exchange.test","key.queue.delayed",messageToSend,
                message1 -> {
                    // 设置过期时间
                    message1.getMessageProperties().setHeader("x-delay",20000);
                    return message1;
                },
                correlationData);
    }
}