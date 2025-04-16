package br.com.project.tasktracker.controller;

import br.com.project.tasktracker.model.Task;
import br.com.project.tasktracker.storage.TaskStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador responsável por gerenciar a lista de tarefas.
 */
public class TaskController {
    private List<Task> tasks;

    public TaskController() {
        tasks = TaskStorage.loadTasks();
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public List<Task> getTasksByStatus(String status) {
        return tasks.stream()
                .filter(task -> task.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public void addTask(Task task) {
        tasks.add(task);
        TaskStorage.saveTasks(tasks);
    }

    public void updateTask(Task updatedTask) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == updatedTask.getId()) {
                tasks.set(i, updatedTask);
                break;
            }
        }
        TaskStorage.saveTasks(tasks);
    }

    public void markInProgress(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                task.setStatus("in-progress");
                break;
            }
        }
        TaskStorage.saveTasks(tasks);
    }

    public void markTaskDone(int taskId) {
        for (Task task : tasks) {
            if (task.getId() == taskId) {
                task.setStatus("done");
                break;
            }
        }
        TaskStorage.saveTasks(tasks);
    }

    public void deleteTask(int id) {
        tasks.removeIf(task -> task.getId() == id);
        TaskStorage.saveTasks(tasks);
    }

    public List<Task> getTasksNotDone() {
        List<Task> filtered = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.getStatus().equalsIgnoreCase("done")) {
                filtered.add(task);
            }
        }
        return filtered;
    }


}