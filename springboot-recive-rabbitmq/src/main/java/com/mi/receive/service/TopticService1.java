package com.mi.receive.service;

import org.springframework.amqp.core.Message;

/**
 * @author : Rong
 * @date : 2021/5/1
 * @Desc:
 */
public interface TopticService1 {

    public void receive(Message message);
}
