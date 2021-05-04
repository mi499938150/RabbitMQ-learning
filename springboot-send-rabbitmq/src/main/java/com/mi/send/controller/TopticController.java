package com.mi.send.controller;

import com.mi.send.service.TopticService;
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
public class TopticController {

    @Autowired
    private TopticService topticService;

    @GetMapping("/toptic")
    public void topticSendOrder(){
        for (int i = 0; i < 10; i++) {
            topticService.topicSendMessage();
        }
    }
}