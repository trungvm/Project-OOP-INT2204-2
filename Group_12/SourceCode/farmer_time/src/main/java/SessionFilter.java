import jakarta.servlet.Filter;
import java.io.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class SessionFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);

        if (session != null && session.getAttribute("email") != null) {
            long sessionAge = System.currentTimeMillis() - session.getLastAccessedTime();
            if (sessionAge > 3600 * 1000) { // 1 hour
                session.removeAttribute("email");
                session.invalidate();
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.sendRedirect("login.jsp");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
