package com.mi.recive.service.impl;



import com.mi.recive.service.FanoutService3;
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
public class FanoutServiceImpl3 implements FanoutService3 {

    @RabbitListener(
            containerFactory = "rabbitListenerContainerFactory",
            bindings = {
                    @QueueBinding(
                            value = @Queue(name = "fanout.queue.test3"),
                            exchange = @Exchange(name = "fanout.exchange.test",
                                    type = ExchangeTypes.FANOUT),
                            key = ""
                    )
            }
    )
    @Override
    public void fanoutRecive3(@Payload Message message) {
        log.info("========fanout3接受消息===========");
        String messageBody = new String(message.getBody());
        log.info(" body = {} " ,messageBody);
    }
}