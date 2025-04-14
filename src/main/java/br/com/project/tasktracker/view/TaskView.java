package br.com.project.tasktracker.view;

import br.com.project.tasktracker.controller.TaskController;
import br.com.project.tasktracker.model.Task;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Interface gráfica principal da aplicação Task Tracker.
 * Exibe as tarefas organizadas por status (To Do, In Progress, Done)
 * em três colunas estilo Trello.
 */
public class TaskView extends Application {

    private TaskController controller = new TaskController();

    private VBox todoColumn = new VBox(10);
    private VBox doingColumn = new VBox(10);
    private VBox doneColumn = new VBox(10);

    @Override
    public void start(Stage primaryStage) {
        HBox root = new HBox(20);
        root.setPadding(new Insets(10));

        setupColumn(todoColumn, "To Do", "todo");
        setupColumn(doingColumn, "Doing", "in-progress");
        setupColumn(doneColumn, "Done", "done");

        root.getChildren().addAll(todoColumn, doingColumn, doneColumn);

        Scene scene = new Scene(root, 800, 400);
        primaryStage.setTitle("Task Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();

        updateAllColumns();
    }

    /**
     * Configura uma coluna da interface com título e botão para adicionar tarefas.
     *
     * @param column VBox onde os elementos serão inseridos
     * @param title  Título da coluna
     * @param status Status correspondente a essa coluna (todo, in-progress, done)
     */
    private void setupColumn(VBox column, String title, String status) {
        column.setPadding(new Insets(10));
        column.setStyle("-fx-border-color: lightgray; -fx-background-color: #f9f9f9; -fx-border-radius: 8; -fx-background-radius: 8;");
        column.setPrefWidth(250);

        Label header = new Label(title);
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        TextField input = new TextField();
        input.setPromptText("Nova tarefa...");

        Button addButton = new Button("Adicionar");
        addButton.setOnAction(e -> {
            String desc = input.getText().trim();
            if (!desc.isEmpty() && status.equals("todo")) {
                Task task = new Task(
                        controller.getAllTasks().size() + 1,
                        desc,
                        "todo",
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );
                controller.addTask(task);
                input.clear();
                updateAllColumns();
            }
        });

        column.getChildren().addAll(header);
        if (status.equals("todo")) {
            column.getChildren().addAll(input, addButton);
        }
    }

    /**
     * Atualiza todas as colunas com base no status das tarefas.
     */
    private void updateAllColumns() {
        updateColumn(todoColumn, controller.getTasksByStatus("todo"), "todo");
        updateColumn(doingColumn, controller.getTasksByStatus("in-progress"), "in-progress");
        updateColumn(doneColumn, controller.getTasksByStatus("done"), "done");
    }

    /**
     * Atualiza visualmente uma coluna com as tarefas correspondentes.
     *
     * @param column VBox da coluna
     * @param tasks  Lista de tarefas
     * @param status Status da coluna
     */
    private void updateColumn(VBox column, List<Task> tasks, String status) {
        // Remove todos os nós exceto o título e botões de adicionar (índices 0 a 2 se for "todo")
        column.getChildren().removeIf(node -> VBox.getMargin(node) != null);

        for (Task task : tasks) {
            VBox taskCard = new VBox();
            taskCard.setPadding(new Insets(5));
            taskCard.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-background-radius: 5; -fx-border-radius: 5;");

            Label desc = new Label(task.getDescription());
            desc.setWrapText(true);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            Label timestamp = new Label("Criado: " + task.getCreatedAt().format(formatter));

            Button moveButton = new Button();
            if (status.equals("todo")) {
                moveButton.setText("➡ Em andamento");
                moveButton.setOnAction(e -> {
                    controller.markInProgress(task.getId());
                    updateAllColumns();
                });
            } else if (status.equals("in-progress")) {
                moveButton.setText("✔ Concluir");
                moveButton.setOnAction(e -> {
                    controller.markTaskDone(task.getId());
                    updateAllColumns();
                });
            }

            Button editButton = new Button("✏️ Editar");
            editButton.setOnAction(e -> {
                TextInputDialog dialog = new TextInputDialog(task.getDescription());
                dialog.setTitle("Editar Tarefa");
                dialog.setHeaderText("Editar descrição da tarefa:");
                dialog.setContentText("Descrição:");

                dialog.showAndWait().ifPresent(newDesc -> {
                    if (!newDesc.trim().isEmpty()) {
                        task.setDescription(newDesc.trim());
                        controller.updateTask(task);
                        updateAllColumns();
                    }
                });
            });

            Button deleteButton = new Button("🗑 Excluir");
            deleteButton.setOnAction(e -> {
                controller.deleteTask(task.getId());
                updateAllColumns();
            });

            taskCard.getChildren().addAll(desc, timestamp);
            if (!status.equals("done")) taskCard.getChildren().addAll(moveButton, editButton);
            taskCard.getChildren().add(deleteButton);

            VBox.setMargin(taskCard, new Insets(5, 0, 5, 0));
            column.getChildren().add(taskCard);
        }
    }
}
