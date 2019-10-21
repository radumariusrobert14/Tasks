package com.example.application.tasks;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<Task>> allTasks;

    public ViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allTasks = repository.getAllTasks();
    }

    public void insert(Task task) {
        repository.insert(task);
    }

    public void delete(Task task) {
        repository.delete(task);
    }

    public void update(Task task) {
        repository.update(task);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }
}
