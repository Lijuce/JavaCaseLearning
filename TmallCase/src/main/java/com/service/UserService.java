package com.service;

import com.pojo.User;

import java.util.List;

/**
 * 用户业务层接口
 */
public interface UserService {

    // 按照用户ID查询用户
    User findUserById(Integer id);

//    // 按照用户姓名查询用户
//    User findUserByName(String name);

    // 查询所有用户
    List<User> findAllUser();

    // 添加新用户
    void addNewUser(User user);

    // 更改用户信息
    void updateUserInfo(User user);
}
