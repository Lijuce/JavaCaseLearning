package web;

import bean.User;
import dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String password = req.getParameter("password");

        UserDAO userDAO = new UserDAO();
        boolean exist = userDAO.isExist(name);
        if (exist) {
            req.setAttribute("msg", "用户名已存在，请重新命名");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }

        User userAdd = new User();
        userAdd.setName(name);
        userAdd.setPassword(password);

        boolean addFlag = false;
        addFlag = userDAO.add(userAdd);
        if (addFlag == true){
            req.setAttribute("msg", "注册成功");
            req.getRequestDispatcher("registerSuccess.jsp").forward(req, resp);
        }else {
            req.setAttribute("msg", "注册失败！！！");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
        }
    }
}
