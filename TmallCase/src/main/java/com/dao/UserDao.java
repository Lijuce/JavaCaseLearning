package com.dao;

import com.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户DAO接口
 */
//@Repository
//public interface UserDao {
//
//
//
//    User findUserById(Integer id);
//
////    @Select("select * from user")
//    List<User> findAllUser();
//
//    @Insert("insert into user (name, password) values(#{name}, #{password})")
//    void addNewUser(User user);
//
//    void updateUserInfo(User user);
//}

public interface UserDao {
    void addNewUser(User user);
}