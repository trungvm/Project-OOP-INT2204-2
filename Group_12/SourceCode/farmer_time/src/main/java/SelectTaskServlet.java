import java.io.*;

import information.TaskInfo;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "selectTask", urlPatterns = "/selectTask")
public class SelectTaskServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int taskId = Integer.parseInt(request.getParameter("taskId"));
        TaskInfo taskInfo = null;

        try {
            Connection conn = ConnectMySQL.getConnection(ConnectMySQL.DB_URL, ConnectMySQL.USER_NAME,
                    ConnectMySQL.PASSWORD);

            String sql = "SELECT * FROM task WHERE task_id = ?";

            // create statement
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, taskId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int projectId = rs.getInt(2);
                String taskName = rs.getString(3);
                String startTime = rs.getString(5);
                String finishTime = rs.getString(6);
                String status = rs.getString(7);
                if (status == null)
                    status = "";
                String priority = rs.getString(8);
                if (priority == null)
                    priority = "";

                if(taskName != null)taskName.replaceAll("\n", "<br>");
                taskInfo = new TaskInfo(taskId, taskName, projectId, priority, status, startTime, finishTime);
            }

            stmt.close();
            conn.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

        request.setAttribute("taskInfo", taskInfo);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/editTask.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
