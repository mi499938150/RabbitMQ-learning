package com.mi.recive.service;

import org.springframework.amqp.core.Message;

/**
 * @author : Rong
 * @date : 2021/5/1
 * @Desc:
 */
public interface FanoutService2 {

    public void fanoutRecive2(Message message);
}
