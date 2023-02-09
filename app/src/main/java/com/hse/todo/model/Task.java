package com.hse.todo.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "task_table")
public class Task {
    @ColumnInfo(name = "task_id")
    @PrimaryKey(autoGenerate = true)
    public long taskId;

    public String task;

    public Priority priority;

    @ColumnInfo(name = "due_date")
    public Date dueDate;

    public Date created;

    @ColumnInfo(name = "is_done")
    public boolean isDone;


    public Task(String task, Priority priority, Date dueDate, Date created, boolean isDone) {
        this.task = task;
        this.priority = priority;
        this.dueDate = dueDate;
        this.created = created;
        this.isDone = isDone;
    }


    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }


    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", task='" + task + '\'' +
                ", priority=" + priority +
                ", dueDate=" + dueDate +
                ", created=" + created +
                ", isDone=" + isDone +
                '}';
    }
}
