package br.com.project.tasktracker.model;

import java.time.LocalDateTime;

/**
 * Representa uma tarefa com descrição, status, data de criação e última modificação.
 */
public class Task {
    private int id;
    private String description;
    private String status; // "todo", "in-progress", "done"
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task(int id, String description, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getDescription() { return description; }

    /**
     * Atualiza a descrição da tarefa e a data de modificação.
     */
    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public String getStatus() { return status; }

    /**
     * Atualiza o status da tarefa e a data de modificação.
     */
    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    private void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
