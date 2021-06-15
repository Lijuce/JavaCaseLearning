package com.controller;

import com.pojo.User;
import com.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Struct;

@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @param model
     * @param refer
     * @return
     */
    @RequestMapping("login")
    public String loginFunc(@RequestParam("name") String username, String password, HttpSession session, Model model, String refer){

        if (refer != null) {
            model.addAttribute("refer",refer);
        }
        User loginUser = loginService.selectByName(username);
        // 账户不存在
        if (loginUser == null){
            model.addAttribute("msg", "账户不存在");
            System.out.println("账户不存在");
            return "login";
        }
        String realPwd = loginUser.getPassword();
        String inputPwd = password;

        if (inputPwd.equals(realPwd)){
            session.setAttribute("user", loginUser);
            return "redirect:"+refer;
        }
        else {
            model.addAttribute("msg", "用户名或密码错误");
            System.out.println("用户名或密码错误");
            return "login";
        }
    }

    @RequestMapping("loginAjax")
    public String loginAjax(@RequestParam("name") String username, String password, HttpSession session, Model model, String refer){
        User loginUser = loginService.selectByName(username);
        // 账户不存在
        String realPwd = loginUser.getPassword();
        System.out.println(realPwd);
        String inputPwd = password;
        System.out.println(inputPwd);
//        if (inputPwd.equals(realPwd)){
        session.setAttribute("user", loginUser);
        return "redirect:/";
//        }
//        else {
//            model.addAttribute("msg", "用户名或密码错误");
//            System.out.println("用户名或密码错误");
//            return "login";
//        }
    }

    /**
     * 用户登出
     * @param request
     * @return
     */
    @RequestMapping("/logout")
    public String logoutFunc(HttpServletRequest request){
        HttpSession session = request.getSession();
        System.out.println(session.getAttribute("user"));
        session.removeAttribute("user");
        return "redirect:/";
    }
}
