package com.hse.todo.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.hse.todo.model.Priority;
import com.hse.todo.model.Task;
import com.hse.todo.util.TaskDB;

import java.util.List;

public class Repository {
    private final TaskDao taskDao;
    private  final LiveData<List<Task>> allTasks;

    public Repository(Application application) {
        TaskDB db = TaskDB.getDB(application);
        taskDao = db.taskDao();
        allTasks = taskDao.getTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insert(Task task) {
        TaskDB.dbWriterExecutor.execute( () -> taskDao.insertTask(task));
    }

    public LiveData<Task> get(long id) { return taskDao.get(id);}

    public void update(Task task){

        TaskDB.dbWriterExecutor.execute(() -> taskDao.update(task));
    }

    public void delete(Task task) {

        TaskDB.dbWriterExecutor.execute(() -> taskDao.delete(task));
    }
}
