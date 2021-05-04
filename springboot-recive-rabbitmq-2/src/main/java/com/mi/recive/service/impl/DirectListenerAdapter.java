package com.mi.recive.service.impl;

import com.mi.recive.service.DirectService2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * @author : Rong
 * @date : 2021/5/4
 * @Desc:
 */
@Service
@Slf4j
public class DirectListenerAdapter {

    // 默认方法
    public void handleMessage(byte[] body) {
        log.info("默认处理方法，message：" + new String(body));
    }

    //HashMap 方法
    public  void directListenerMethod1(String message){
        String messageBody = new String(message);
        log.info("handleMessage:messageBody:{}", messageBody);
    }
}