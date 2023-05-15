import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "login", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Kiểm tra thông tin đăng nhập từ database
        boolean isValid = checkLogin(email, password);

        if (isValid) {
            // Lưu thông tin phiên đăng nhập
            HttpSession session = request.getSession();
            session.setAttribute("email", email);

            // Chuyển hướng đến trang showProject
            response.sendRedirect("showProject");
        } else {
            // Thông báo đăng nhập không hợp lệ
            request.setAttribute("errorMessage", "Invalid email or password");
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private boolean checkLogin(String email, String password) {
        boolean isValid = false;
        try {
        // Kết nối đến database
        Connection conn = ConnectMySQL.getConnection(ConnectMySQL.DB_URL, ConnectMySQL.USER_NAME,
                ConnectMySQL.PASSWORD);

        // Kiểm tra thông tin đăng nhập từ bảng user_profile
        String sql = "SELECT * FROM user_profile WHERE email = ? AND password = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, email);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();

        isValid = rs.next();

        // Đóng kết nối và trả về kết quả
        rs.close();
        stmt.close();
        conn.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return isValid;
    }
}
