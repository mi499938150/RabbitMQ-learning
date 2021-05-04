package com.mi.send.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mi.send.dto.OrderMessageDTO;
import com.mi.send.service.FanoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author : Rong
 * @date : 2021/5/1
 * @Desc:
 */

@Slf4j
@Service
public class FanoutServiceImpl implements FanoutService {


    @Autowired
    private RabbitTemplate rabbitTemplate;


    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void sendFanoutMessage() {

        log.info("==========发送Fanout类型消息=======");
        try {
            // 第一种方式
            OrderMessageDTO orderMessageDTO = new OrderMessageDTO();
            orderMessageDTO.setOrderId(1);
            orderMessageDTO.setPrice(new BigDecimal("20"));
            orderMessageDTO.setProductId(100);
            String messageToSend = objectMapper.writeValueAsString(orderMessageDTO);
            // 发送端确认是否确认消费
            CorrelationData correlationData = new CorrelationData();
            // 唯一ID
            correlationData.setId(orderMessageDTO.getOrderId().toString());
            // 发送
            rabbitTemplate.convertAndSend("fanout.exchange.test","",messageToSend,correlationData);
            log.info("发送成功");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}