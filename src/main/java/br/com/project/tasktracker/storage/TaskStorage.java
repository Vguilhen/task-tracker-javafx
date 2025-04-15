package br.com.project.tasktracker.storage;

import br.com.project.tasktracker.model.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.project.tasktracker.storage.LocalDateTimeAdapter;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonParseException;

/**
 * Persistência das tarefas em JSON.
 */
public class TaskStorage {

    private static final String FILE_PATH = "tasks.json";
    static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .setPrettyPrinting()
            .create();

    List<Task> tasks = gson.fromJson(FILE_PATH, new TypeToken<List<Task>>() {}.getType());



    public static void saveTasks(List<Task> tasks) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(tasks, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar tarefas: " + e.getMessage());
        }
    }

    public static List<Task> loadTasks() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<Task>>() {
            }.getType();

            List<Task> tasks = gson.fromJson(reader, listType);

            return tasks != null ? tasks : new ArrayList<>();

        } catch (IOException e) {
            System.err.println("Erro ao carregar tarefas: " + e.getMessage());
            return new ArrayList<>();
        }

    }


}
