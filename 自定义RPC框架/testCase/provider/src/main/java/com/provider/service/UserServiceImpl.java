package com.provider.service;

import lijuce.rpc.annotation.Service;
import lijuce.rpc.service.UserService;

/**
 * @author Lijuce
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public void getName(){
        System.out.println("call the provider .....");
    }
}
