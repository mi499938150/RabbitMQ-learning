package com.mi.receive.service.Impl;

import com.mi.receive.service.FanoutService1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * @author : Rong
 * @date : 2021/5/1
 * @Desc:
 */
@Service
@Slf4j
public class FanoutServiceImpl1 implements FanoutService1 {

    @RabbitListener(
            containerFactory = "rabbitListenerContainerFactory",
            bindings = {
                    @QueueBinding(
                            value = @Queue(name = "fanout.queue.test1"),
                            exchange = @Exchange(name = "fanout.exchange.test",
                                    type = ExchangeTypes.FANOUT),
                            key = ""
                    )
            }
    )
    @Override
    public void fanoutRecive1(@Payload Message message) {
        log.info("========fanout接受消息===========");
        String messageBody = new String(message.getBody());
        log.info(" body = {} " ,messageBody);
    }
}