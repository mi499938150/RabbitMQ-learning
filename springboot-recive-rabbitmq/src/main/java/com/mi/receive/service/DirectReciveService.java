package com.mi.receive.service;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author : Rong
 * @date : 2021/5/1
 * @Desc:
 */
public interface DirectReciveService {

    public void DirectRecive(Message message);


    public void QosDirectRecive(Message message, Channel channel) throws IOException;

    // 处理异常的消息重回队列
    public void sendErroBackQueueMessage(Message message, Channel channel) throws IOException;

    // 接收队列过期队列消息
//    public void queueTimeOutMessage(@Payload Message message, Channel channel) throws IOException;

    // 监听死信队列， 死信队列可以在任何的队列上被指定，实际上就是设置某个队列的属性
    public void deadQueueListenter(Message message, Channel channel) throws IOException;

    // 监听延迟队列，
    public void delayedQueueListenter(Message message, Channel channel) throws IOException;
}
