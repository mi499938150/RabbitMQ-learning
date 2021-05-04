package com.mi.send.service;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author : Rong
 * @date : 2021/5/4
 * @Desc:
 */
public interface CustomService {

    /********************************/
    // 自定义MessageListenerAdapter

    public void directQueue01()throws JsonProcessingException;


    public void fanoutQueue01() throws JsonProcessingException;
}
