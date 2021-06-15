package com.service.Impl;

import com.dao.UserDao;
import com.pojo.User;
import com.service.LoginService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户业务层实现类
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

//    @Override
//    public User selectByUser(User user) {
//        List<User> userByName = userDao.findUserByName(user.getName());
//        if (userByName.isEmpty())
//            return null;
//        else
//            return userByName.get(0);
//    }

//    @Override
//    public User selectByName(String name) {
//        List<User> users = userDao.findUserByName(name);
//        if (users.isEmpty())
//            return null;
//        else
//            return users.get(0);
//    }

    @Override
    public User findUserById(Integer id) {
        System.out.println("按照用户ID查询用户");
        return null;
    }

    @Override
    public List<User> findAllUser() {
        System.out.println("查询所有用户");
        return null;
    }

    @Override
    public void addNewUser(User user) {
        userDao.addNewUser(user);
    }

    @Override
    public void updateUserInfo(User user) {
        System.out.println("更改用户信息");
    }
}
