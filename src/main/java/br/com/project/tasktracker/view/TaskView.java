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

        Scene scene = new Scene(root, 900, 500);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

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
        column.setPadding((new Insets(10)));
        column.setPrefWidth(280);
        column.setStyle("-fx-background-color: #f0f4f8; -fx-background-radius: 10; -fx-border-color: #d0d0d0; -fx-border-radius: 10;");

        Label header = new Label(title);
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #2c3e50;");

        TextField input = new TextField();
        input.setPromptText("Nova tarefa...");

        Button addButton = new Button("➕ Adicionar");
        addButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");;

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
            VBox taskCard = new VBox(5);
            taskCard.setPadding(new Insets(5));
            taskCard.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 1);");

            Label desc = new Label(task.getDescription());
            desc.setWrapText(true);
            desc.setStyle("-fx-font-size: 14px; -fx-text-fill: #2c3e50;");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            Label timestamp = new Label("Criado: " + task.getCreatedAt().format(formatter));
            timestamp.setStyle("-fx-font-size: 11px; -fx-text-fill: gray;");

            HBox buttonBox = new HBox(5);

            if (status.equals("todo")) {
                Button moveButton = new Button();
                moveButton.setText("➡ Em andamento");
                moveButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
                moveButton.setMinWidth(130);
                moveButton.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(moveButton, Priority.ALWAYS);
                moveButton.setOnAction(e -> {
                    controller.markInProgress(task.getId());
                    updateAllColumns();
                });
                buttonBox.getChildren().add(moveButton);
            } else if (status.equals("in-progress")) {
                Button moveButton = new Button("✔ Concluir");
                moveButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
                moveButton.setMinWidth(100);
                moveButton.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(moveButton, Priority.ALWAYS);
                moveButton.setOnAction(e -> {
                    controller.markTaskDone(task.getId());
                    updateAllColumns();
                });
                buttonBox.getChildren().add(moveButton);
            }

            Button editButton = new Button("✏️ Editar");
            editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
            editButton.setMinWidth(100);
            editButton.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(editButton, Priority.ALWAYS);
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
            deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
            deleteButton.setMinWidth(100);
            deleteButton.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(deleteButton, Priority.ALWAYS);
            deleteButton.setOnAction(e -> {
                controller.deleteTask(task.getId());
                updateAllColumns();
            });

            if (!status.equals("done")) buttonBox.getChildren().add(editButton);
            buttonBox.getChildren().add(deleteButton);

            taskCard.getChildren().addAll(desc, timestamp, buttonBox);
            VBox.setMargin(taskCard, new Insets(5, 0, 5, 0));
            column.getChildren().add(taskCard);
        }
    }
}
