package br.com.project.tasktracker.view;

import br.com.project.tasktracker.controller.TaskController;
import br.com.project.tasktracker.model.Task;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class TaskView extends Application {

    //@param stage Janela principal (stage) do JavaFx.
    private TaskController controller = new TaskController();

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10); // Layout vertical com espaçamento de 10px
        TextField taskInput = new TextField(); // Campo de entrada para o nome da tarefa
        Button addButton = new Button("Adicionar tarefa"); // Botão para adicionar
        Button completeButton = new Button("Marcar como concluída");
        Button editButton = new Button("Editar");
        Button deleteButton = new Button("Excluir");
        ListView<String> taskList = new ListView<>(); // Lista visual das tarefas

        // Define o que acontece ao clicar no botão
        addButton.setOnAction(e -> {
            String desc = taskInput.getText();
            if (!desc.isEmpty()) {
                // Cria uma nova tarefa com status "todo" e timestamps
                Task task = new Task(
                        controller.getAllTasks().size() + 1,
                        desc,
                        "todo",
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );


                controller.addTask(task); // Adiciona a tarefa no controlador
                taskList.getItems().add(task.getDescription()); // Atualiza a interface
                taskInput.clear(); // Limpa o campo de texto

            }
        });

        completeButton.setOnAction(e -> {
            int selectedIndex = taskList.getSelectionModel().getSelectedIndex();
            if (selectedIndex != -1) {
                Task task = controller.getAllTasks().get(selectedIndex);
                controller.markTaskDone(task.getId());

                taskList.getItems().set(selectedIndex, "✔ " + task.getDescription());
            }
        });

        editButton.setOnAction(e -> {
            String selected = taskList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                TextInputDialog dialog = new TextInputDialog(selected);
                dialog.setTitle("Editar Tarefa");
                dialog.setHeaderText(null);
                dialog.setContentText("Nova descrição");

                dialog.showAndWait().ifPresent(newDesc -> {
                    int index = taskList.getSelectionModel().getSelectedIndex();
                    Task task = controller.getAllTasks().get(index);
                    controller.updateTask(task.getId(), newDesc);
                    taskList.getItems().set(index, newDesc);
                });
            }
        });

        deleteButton.setOnAction(e -> {
            int index = taskList.getSelectionModel().getSelectedIndex();
            if (index >=0) {
                Task task = controller.getAllTasks().get(index);
                controller.deleteTask(task.getId());
                taskList.getItems().remove(index);
            }
        });

        // Adiciona todos os elementos ao layout
        root.getChildren().addAll(
                new Label("Nova tarefa:"),
                taskInput,
                addButton,
                completeButton,
                editButton,
                deleteButton,
                taskList
        );


        // Configura e exibe a janela principal
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Task Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();


    }


}
