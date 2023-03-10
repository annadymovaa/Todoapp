package com.hse.todo.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hse.todo.data.Repository;

import java.util.List;

public class TaskView extends AndroidViewModel {
    public static Repository repository;
    public final LiveData<List<Task>> allTasks;


    public TaskView(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allTasks = repository.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {return allTasks;}
    public static void  insert(Task task) {repository.insert(task);}
    public LiveData<Task> get(long id) {return repository.get(id);}
    public static void update(Task task) {repository.update(task);}
    public static void delete(Task task) {repository.delete(task);}

}
