package com.service.Impl;

import com.dao.LoginDao;
import com.pojo.User;
import com.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginDao loginDao;

    @Override
    public User selectByName(String name) {
        List<User> users = loginDao.findUserByName(name);
        if (users.isEmpty())
            return null;
        else{
            return users.get(0);
        }
    }

    @Override
    public User selectByUser(User user) {
        return null;
    }


}
