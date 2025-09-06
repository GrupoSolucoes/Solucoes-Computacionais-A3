package controller;

import dao.TaskDAO;
import model.Task;
import java.util.List;

public class TaskController {
    private final TaskDAO taskDAO;

    public TaskController() {
        taskDAO = new TaskDAO();
    }

    public void addTask(Task task) {
        taskDAO.addTask(task);
    }

    public void updateTask(Task task) {
        taskDAO.updateTask(task);
    }

    public void deleteTask(int id) {
        taskDAO.deleteTask(id); // Agora este método existe no DAO
    }

    public List<Task> getAllTasks() {
        return taskDAO.getAllTasks();
    }
}