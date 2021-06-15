package com.service;

import com.pojo.User;
import org.springframework.stereotype.Repository;

public interface LoginService {
    // 根据用户名获取用户信息
    User selectByName(String name);

    User selectByUser(User user);
}
