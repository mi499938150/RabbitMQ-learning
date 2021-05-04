package com.mi.recive.service.impl;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author : Rong
 * @date : 2021/5/4
 * @Desc:
 */
@Service
@Slf4j
public class FanoutListenerAdapter {

    public void fanoutListenerMethod2( String str) throws IOException {
        String messageBody = new String(str);
        log.info("fanoutListenerMethod2:messageBody:{}", messageBody);
    }
}