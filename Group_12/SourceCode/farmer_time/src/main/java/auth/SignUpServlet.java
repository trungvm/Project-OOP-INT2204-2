package auth;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.sql.Connection;
import java.sql.PreparedStatement;

import config.ConnectMySQL;

@WebServlet(name = "signup", urlPatterns = "/signup")
public class SignUpServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = (String) request.getParameter("name");

        String email = (String) request.getParameter("email");

        String password = (String) request.getParameter("password");
        String repeat = (String) request.getParameter("repeat");

        try {
            if (password.equals(repeat)) {
                Connection conn = ConnectMySQL.getConnection(ConnectMySQL.DB_URL, ConnectMySQL.USER_NAME,
                        ConnectMySQL.PASSWORD);

                String sql = "INSERT INTO user_profile(user_name, email, password) VALUES(?, ?, ?)";

                // create statement
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, password);

                stmt.execute();

                stmt.close();
                conn.close();
                response.sendRedirect("login.jsp");
            } else
                response.sendRedirect("signup.jsp");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
