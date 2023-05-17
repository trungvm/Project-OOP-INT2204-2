package Task;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.sql.Connection;
import java.sql.PreparedStatement;

import config.ConnectMySQL;

@WebServlet(name = "editTask", urlPatterns = "/editTask")
public class EditTaskServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int projectId = Integer.parseInt(request.getParameter("projectId"));
        int taskId = Integer.parseInt(request.getParameter("taskId"));
        String taskName = (String) request.getParameter("taskName");
        taskName = taskName.trim();
        if (taskName == "")
            taskName = "No title";

        String priority = (String) request.getParameter("priority");
        String status = (String) request.getParameter("status");

        String startTime = (String) request.getParameter("startTime");
        if (startTime.isEmpty())
            startTime = null;

        String finishTime = (String) request.getParameter("finishTime");
        if (finishTime.isEmpty())
            finishTime = null;

        try {
            Connection conn = ConnectMySQL.getConnection(ConnectMySQL.DB_URL, ConnectMySQL.USER_NAME,
                    ConnectMySQL.PASSWORD);

            String sql = "UPDATE task SET task_name = ?, priority = ?, status = ?, start_date = ?, finish_date = ? WHERE task_id = ?";

            // create statement
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, taskName);
            stmt.setString(2, priority);
            stmt.setString(3, status);
            stmt.setString(4, startTime);
            stmt.setString(5, finishTime);
            stmt.setInt(6, taskId);

            stmt.execute();

            stmt.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        request.setAttribute("projectId", projectId);

        RequestDispatcher dispatcher = request.getRequestDispatcher("showTask");
        dispatcher.forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
