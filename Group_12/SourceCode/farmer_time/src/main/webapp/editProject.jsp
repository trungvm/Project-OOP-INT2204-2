<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="java.util.*, java.io.*, information.ProjectInfo" %>
        <%@ page import="java.text.SimpleDateFormat" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="utf-8">
                <title>Edit Project</title>
                <!-- Mobile Specific Metas -->
                <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" />

                <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.5.0/css/all.css"
                    integrity="sha384-B4dIYHKNBt8Bc12p+WXckhzcICo0wtJAoU8YZTY5qE0Id1GSseTk6S+L3BlXeVIU"
                    crossorigin="anonymous" />
                <!-- Font-->
                <link rel="stylesheet" type="text/css" href="./css/roboto.css">
                <!-- Main Style Css -->
                <link rel="stylesheet" href="./css/form.css" />
            </head>

            <% ProjectInfo projectInfo=(ProjectInfo) request.getAttribute("projectInfo"); %>

                <body class="form-v5">
                    <jsp:include page="components/header.jsp" />
                    <div class="page-content">
                        <div class="form-v5-content">
                            <form id="formCancel" action="showTask" method="post" hidden>
                                <input hidden type="text" name="projectId" value="<%= projectInfo.projectId %>" />
                                <button type="submit"></button>
                            </form>

                            <form id="formSave" class="form-detail" action="editProject" method="post">
                                <h2>Edit Project</h2>
                                <input hidden type="text" name="projectId" value="<%= projectInfo.projectId %>" />
                                <div class="mb-3">
                                    <label class="form-label">Project Name</label>
                                    <input type="text" onkeypress="return event.keyCode != 13;" class="form-control"
                                        name="projectName" value="<%= projectInfo.projectName %>" />
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Descriptions</label>
                                    <textarea type="text" class="form-control" name="des"
                                        style="height: 80px;"><%= projectInfo.description %></textarea>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Image</label>
                                    <textarea type="text" class="form-control"
                                        name="img"><%= projectInfo.img %></textarea>
                                </div>

                                <div class="row mb-4">
                                    <div class="col">
                                        <div class="mb-3">
                                            <label class="form-label">Priority</label>
                                            <select name="priority" class="form-control border">
                                                <option value="<%= projectInfo.priority %>">
                                                    <%= projectInfo.priority %>
                                                </option>
                                                <% if(!projectInfo.priority.equals("LOW")) { %>
                                                    <option value="LOW">LOW</option>
                                                    <% } %>
                                                        <% if(!projectInfo.priority.equals("MEDIUM")) { %>
                                                            <option value="MEDIUM">MEDIUM</option>
                                                            <% } %>
                                                                <% if(!projectInfo.priority.equals("HIGH")) { %>
                                                                    <option value="HIGH">HIGH</option>
                                                                    <% } %>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col">
                                        <div class="mb-3">
                                            <label class="form-label">Status</label>
                                            <select name="status" class="form-control border">
                                                <option value="<%= projectInfo.status %>">
                                                    <%= projectInfo.status %>
                                                </option>
                                                <% if(!projectInfo.status.equals("INPROGRESS")) { %>
                                                    <option value="INPROGRESS">INPROGRESS</option>
                                                    <% } %>
                                                        <% if(!projectInfo.status.equals("STOPPED")) { %>
                                                            <option value="STOPPED">STOPPED</option>
                                                            <% } %>
                                                                <% if(!projectInfo.status.equals("COMPLETED")) { %>
                                                                    <option value="COMPLETED">COMPLETED</option>
                                                                    <% } %>
                                            </select>
                                        </div>
                                    </div>
                                </div>

                                <div class="row mb-4">
                                    <div class="col">
                                        <div class="form-outline">
                                            <label class="form-label" for="form6Example1">Start Time</label>
                                            <input type="datetime-local" name="startTime"
                                                value="<%= projectInfo.startTime %>" id="form6Example1"
                                                class="form-control" />
                                        </div>
                                    </div>
                                    <div class="col">
                                        <div class="form-outline">
                                            <label class="form-label" for="form6Example2">Finish Time</label>
                                            <input type="datetime-local" name="finishTime"
                                                value="<%= projectInfo.finishTime %>" id="form6Example2"
                                                class="form-control" />
                                        </div>
                                    </div>
                                </div>
                                <button type="submit" hidden></button>
                            </form>

                            <div class="d-block p-3 text-right card-footer">
                                <button class="btn btn-success btn-sm" type="submit"
                                    onclick="document.getElementById('formSave').submit();">Save</button>
                                <button class="mr-2 btn btn-danger btn-sm" type="submit"
                                    onclick="document.getElementById('formCancel').submit();">Cancel</button>
                            </div>
                        </div>
                    </div>
                </body>

            </html>