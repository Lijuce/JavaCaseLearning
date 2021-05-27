package web;

import bean.User;
import dao.UserDAO;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        name = HtmlUtils.htmlEscape(name);  // 字符转义，确保存入数据库的用户名是具有可读性的。
        String password = req.getParameter("password");

        UserDAO userDAO = new UserDAO();
        User user = userDAO.get(name, password);

        if (null == user) {
            req.setAttribute("msg", "账号或密码错误");
            req.getRequestDispatcher("login.jsp").forward(req, resp);  // 登录失败，仍然停留在此 登录界面
            return;
        }
        req.getSession().setAttribute("user", user);  //
        req.getRequestDispatcher("home.jsp").forward(req, resp);  // 登录成功，跳转home界面
    }
}
