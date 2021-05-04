package com.mi.recive.service.impl;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

/**
 * @author : Rong
 * @date : 2021/5/4
 * @Desc:
 */
@Service
@Slf4j
public class FanoutListenerAdapter {


    /**
     * 默认方法名
     * @param str
     */
    public void handleMessage(String str){
        String messageBody = new String(str);
        log.info("fanoutListenerMethod1:messageBody:{}", messageBody);
    }

    public void fanoutListenerMethod1(String str){
        String messageBody = new String(str);
        log.info("fanoutListenerMethod1:messageBody:{}", messageBody);
    }
}