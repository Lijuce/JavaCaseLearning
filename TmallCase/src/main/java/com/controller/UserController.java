package com.controller;

import com.pojo.User;
import com.service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping("/register")
    public String registerFunc(@RequestParam("name") String username, String password ){
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        userService.addNewUser(user);
        System.out.println("controller控制器-注册新用户");
        return "registerSuccess";
    }

    @RequestMapping("/showHome")
    public String loginShow(){
        return "main";
    }
//    @RequestMapping(value = "/loginFunc", method = RequestMethod.POST)
//    public String loginFunc(@RequestParam("name") String username, String password, HttpServletRequest request, Model model){
//        User loginUser = userService.selectByName(username);
//        // 账户不存在
//        if (loginUser == null){
//            model.addAttribute("msg", "账户不存在");
//            System.out.println(username);
//            System.out.println("账户不存在");
//            return "login";
//        }
//        String realPwd = loginUser.getPassword();
//        String inputPwd = password;
//
//        if (inputPwd.equals(realPwd)){
//            HttpSession session = request.getSession();
//            session.setAttribute("user", loginUser);
////            model.addAttribute("user", loginUser);
//            System.out.println("密码正确，登录成功");
//            return "redirect:/home.jsp";
//        }else {
//            model.addAttribute("msg", "用户名或密码错误");
//            System.out.println("用户名或密码错误");
//            return "login";
//        }
//    }
}
