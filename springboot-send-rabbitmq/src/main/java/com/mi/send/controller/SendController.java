package com.mi.send.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mi.send.service.DirectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Rong
 * @date : 2021/5/1
 * @Desc:
 */
@RestController
@Slf4j
@RequestMapping("/api")
public class SendController {

    @Autowired
    private DirectService directService;
    @GetMapping
    public void sendOrder(){
        for (int i = 0; i < 10; i++) {
            directService.sendMessage();
        }
    }

    @GetMapping("/qosDirect")
    public void qosDirect() throws JsonProcessingException {
        for (int i = 0; i < 200; i++) {
            directService.sendQosMessage();
        }
    }

    @GetMapping("/requeueDirect")
    public void requeueDirect() throws JsonProcessingException {
        for (int i = 0; i < 5; i++) {
            directService.requeueMessage(i);
        }
    }

    @GetMapping("/timeoutDirect")
    public void timeoutDirect() throws JsonProcessingException {
        directService.timeOutMessage();
    }

    @GetMapping("/queueTimeOutDirect")
    public void queueTimeOutDirect() throws JsonProcessingException {
        directService.queueTimeOutMessage();
    }

    @GetMapping("/deadQueue")
    public void deadQueue() throws JsonProcessingException {
        directService.deadMessag();
    }

    @GetMapping("/delayedQueue")
    public void delayedQueue() throws JsonProcessingException {
        directService.delayedMessage();
    }
}