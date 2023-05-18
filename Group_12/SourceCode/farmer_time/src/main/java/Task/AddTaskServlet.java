package Task;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.sql.Connection;
import java.sql.PreparedStatement;

import config.ConnectMySQL;

@WebServlet(name = "task", urlPatterns = "/addTask")
public class AddTaskServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int projectId = Integer.parseInt(request.getParameter("projectId"));

        String taskName = (String) request.getParameter("taskName");
        taskName = taskName.trim();
        if (taskName == "")
            taskName = "No title";

        String priority = (String) request.getParameter("priority");
        String status = (String) request.getParameter("status");

        String startTime = (String) request.getParameter("startTime");
        if (startTime != null) {
            if (startTime.isEmpty())
                startTime = null;
        }

        String finishTime = (String) request.getParameter("finishTime");
        if (finishTime != null) {
            if (finishTime.isEmpty())
                finishTime = null;
        }
        try {
            Connection conn = ConnectMySQL.getConnection(ConnectMySQL.DB_URL, ConnectMySQL.USER_NAME,
                    ConnectMySQL.PASSWORD);

            String sql = "INSERT INTO task(project_id, task_name, priority, status, start_date, finish_date) VALUES(?, ?, ?, ?, ?, ?)";

            // create statement
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, projectId);
            stmt.setString(2, taskName);
            stmt.setString(3, priority);
            stmt.setString(4, status);
            stmt.setString(5, startTime);
            stmt.setString(6, finishTime);

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
