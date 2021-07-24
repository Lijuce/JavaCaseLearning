package com.consumer.controller;

import lijuce.rpc.annotation.InjectService;
import lijuce.rpc.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class IndexControl {

    @InjectService
    UserService userService;

    @GetMapping("/user")
    public void consume(){
        userService.getName();
    }
}
