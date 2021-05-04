package com.mi.send.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mi.send.service.CustomService;
import com.mi.send.service.DirectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Rong
 * @date : 2021/5/4
 * @Desc:
 */
@RestController
@Slf4j
@RequestMapping("/api/custom")
public class CustomController {

    @Autowired
    private CustomService customDirectService;

    @GetMapping
    public void sendCustomDirect() throws JsonProcessingException {
            customDirectService.directQueue01();
    }

    @GetMapping("/fanout")
    public void sendCustomFanout() throws JsonProcessingException {
        customDirectService.fanoutQueue01();
    }
}