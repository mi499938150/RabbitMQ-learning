package com.mi.send.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mi.send.dto.OrderMessageDTO;
import com.mi.send.service.CustomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author : Rong
 * @date : 2021/5/4
 * @Desc: MessageListenerAdapter 自定义监听，发送端
 */
@Slf4j
@Service
public class CustomServiceImpl implements CustomService {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 发送端
     * @throws JsonProcessingException
     */
    @Override
    public void directQueue01() throws JsonProcessingException {
        /* 使用MessageProperties传递的对象转换成message*/
        MessageProperties messageProperties = new MessageProperties();
        // 转换成Sting 类型
        messageProperties.setContentType("text/plain");
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
        rabbitTemplate.convertAndSend("dircet.exchange01.test","direct.queue01.key",message,correlationData);
    }

    @Override
    public void fanoutQueue01() throws JsonProcessingException {
        /* 使用MessageProperties传递的对象转换成message*/
        MessageProperties messageProperties = new MessageProperties();
        // 转换成Sting 类型
        messageProperties.setContentType("text/plain");
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
        rabbitTemplate.convertAndSend("fanout.exchange01.test","",message,correlationData);
    }
}