package com.dao;

import com.pojo.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginDao {

    @Select("select * from user where name=#{name}")
    List<User> findUserByName(String name);
}
