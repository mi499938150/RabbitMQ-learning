package com.mi.recive.service;

import org.springframework.amqp.core.Message;

/**
 * @author : Rong
 * @date : 2021/5/1
 * @Desc:
 */
public interface TopticService2 {

    public void receive(Message message);
}
