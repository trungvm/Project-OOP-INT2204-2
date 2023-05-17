package Task;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;


@WebServlet(name = "taskForm", urlPatterns = "/taskForm")
public class TaskFormServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

       int projectId = Integer.parseInt(request.getParameter("projectId"));
       request.setAttribute("projectId", projectId);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/taskForm.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
