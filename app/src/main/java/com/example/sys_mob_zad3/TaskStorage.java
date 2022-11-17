package com.example.sys_mob_zad3;
import android.util.Log;

import com.example.sys_mob_zad3.enums.Category;
import com.example.sys_mob_zad3.models.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskStorage {
    private static final TaskStorage taskStorage = new TaskStorage();

    private final List<Task> tasks = new ArrayList<>();

    public static TaskStorage getInstance() {
        return taskStorage;
    }

    private TaskStorage () {
        Task task;

        for (int i = 0; i < 100; ++i) {
            task = new Task();

            task.setName(String.format("Task #%d", i));
            task.setDone(i % 3 == 0);
            if (i % 3 == 0) {
                task.setCategory(Category.STUDIES);
            } else {
                task.setCategory(Category.HOME);
            }

            tasks.add(task);
        }
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Task getTask(UUID id) {
        for (Task task : tasks) {
            Log.d("UUID", String.format("%s, %s", id.toString(), task.getId().toString()));
            if (id.equals(task.getId())) {
                return task;
            }
        }

        return null;
    }
}
