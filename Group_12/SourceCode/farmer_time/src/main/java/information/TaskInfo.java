package information;

public class TaskInfo {
    public int taskId;
    public String taskName;
    public int projectId;
    public String priority;
    public String status;
    public String startTime;
    public String finishTime;

    public TaskInfo(int taskId, String taskName, int projectId, String priority, String status, String startTime,
            String finishTime) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.projectId = projectId;
        this.priority = priority;
        this.status = status;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }
}
