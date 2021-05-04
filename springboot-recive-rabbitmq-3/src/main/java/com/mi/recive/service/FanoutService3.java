package com.mi.recive.service;

import org.springframework.amqp.core.Message;

/**
 * @author : Rong
 * @date : 2021/5/1
 * @Desc:
 */
public interface FanoutService3 {

    public void fanoutRecive3(Message message);


}
