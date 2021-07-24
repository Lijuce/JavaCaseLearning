package com.consumer.service;

import lijuce.rpc.service.UserService;
import lijuce.rpc.annotation.InjectService;
import org.springframework.stereotype.Service;

@Service
public class ServiceConsume {
    @InjectService
    UserService userService;

    public void consume(){
        userService.getName();
    }
}
