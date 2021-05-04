package com.mi.send.service;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author : Rong
 * @date : 2021/5/1
 * @Desc:
 */
public interface DirectService {

    public void sendMessage();

    //  使用限流Qos
    public void sendQosMessage() throws JsonProcessingException;


    //  使用重回队列
    public void requeueMessage(int i ) throws JsonProcessingException;

    // 消息设置过期时间时间，如下
    public void timeOutMessage() throws JsonProcessingException;

    // 设置 队列过期时间
    public void queueTimeOutMessage() throws JsonProcessingException;

    // 设置 死信队列
    public void deadMessag() throws JsonProcessingException;

    // 延迟队列
    public void delayedMessage() throws JsonProcessingException;




}
