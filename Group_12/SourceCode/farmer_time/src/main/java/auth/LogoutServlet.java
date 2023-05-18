package auth;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "logout", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Xóa toàn bộ thông tin phiên làm việc (session) của người dùng
        HttpSession session = request.getSession(false); // không tạo mới session nếu không tồn tại
        if (session != null) {
            session.removeAttribute("email");
            session.invalidate(); // hủy session
        }

        // Chuyển hướng đến trang đăng nhập
        response.sendRedirect("login.jsp"); 
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
