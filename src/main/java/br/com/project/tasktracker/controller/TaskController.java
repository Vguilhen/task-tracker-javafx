package br.com.project.tasktracker.controller;

import br.com.project.tasktracker.model.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskController {
    private List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public void markTaskDone(int taskId) {
        for (Task task : tasks) {
            if (task.getId() == taskId) {
                task.setStatus("done");
                task.setUpdatedAt(LocalDateTime.now());
                break;
            }
        }
    }

    public void updateTask(int id, String newDescription) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                task.setDescription(newDescription);
                task.setUpdatedAt(LocalDateTime.now());
                break;
            }
        }
    }

    public void deleteTask(int id) {
        tasks.removeIf(task -> task.getId() == id);
    }
}