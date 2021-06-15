package com.pojo;

import java.io.Serializable;

public class User implements Serializable {
    private Integer id;
    private String name;
    private String password;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + id +
                ", username='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setUserId(Integer userId) {
        this.id = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String username) {
        this.name = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
