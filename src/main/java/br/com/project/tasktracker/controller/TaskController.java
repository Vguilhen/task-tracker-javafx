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

    public void markInProgress(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                task.setStatus("in-progress");
                task.setUpdatedAt(LocalDateTime.now());
                break;
            }
        }
    }

    public void updateTask(Task updatedTask) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == updatedTask.getId()) {
                tasks.set(i, updatedTask);
                break;
            }
        }
    }

    public void deleteTask(int id) {
        tasks.removeIf(task -> task.getId() == id);
    }
}