package Project;
import java.io.*;

import information.ProjectInfo;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import config.ConnectMySQL;

@WebServlet(name = "selectProject", urlPatterns = "/selectProject")
public class SelectProjectServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int projectId = Integer.parseInt(request.getParameter("projectId"));
        ProjectInfo projectInfo = null;

        try {
            Connection conn = ConnectMySQL.getConnection(ConnectMySQL.DB_URL, ConnectMySQL.USER_NAME,
                    ConnectMySQL.PASSWORD);

            String sql = "SELECT * FROM project WHERE project_id = ?";

            // create statement
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, projectId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String projectName = rs.getString(3);
                String description = rs.getString(4);
                String img = rs.getString(5);
                String priority = rs.getString(9);
                String status = rs.getString(8);
                String startTime = rs.getString(6);
                String finishTime = rs.getString(7);

                projectInfo = new ProjectInfo(projectName, description, img, priority, status, startTime,
                        finishTime, projectId);
            }

            stmt.close();
            conn.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

        request.setAttribute("projectInfo", projectInfo);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/editProject.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
