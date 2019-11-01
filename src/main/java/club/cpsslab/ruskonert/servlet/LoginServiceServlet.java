package club.cpsslab.ruskonert.servlet;

import club.cpsslab.ruskonert.element.User;

import javax.jws.WebService;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebService
public class LoginServiceServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("login.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // If username is empty
        if(username == null || username.equalsIgnoreCase("")) {
            resp.getWriter().println("Something wrong");
            resp.setStatus(403);
            return;
        }

        // If password is empty
        if(password == null || password.equalsIgnoreCase("")) {
            resp.getWriter().println("Something wrong");
            resp.setStatus(403);
            return;
        }

        User referencedUser = User.getUser(username);
        if(referencedUser == null) {
            req.setAttribute("error_message", "아이디 또는 비밀번호가 일치하지 않습니다!");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}

