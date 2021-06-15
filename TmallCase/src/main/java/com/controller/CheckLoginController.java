package com.controller;

import com.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class CheckLoginController {

    @RequestMapping("checkLogin")
    @ResponseBody
    public String checkLogin(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        System.out.println("调用checkLogin" + user);
        if (null != user){
            return "success";
        }
        return "fail";
    }
}
