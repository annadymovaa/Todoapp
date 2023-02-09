package com.hse.todo.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.hse.todo.data.TaskDao;
import com.hse.todo.model.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class TaskDB extends RoomDatabase {
    public static final int NUMBER_OF_THREADS = 4;
    public static final String DB_NAME = "todo_db";
    private static volatile TaskDB INSTANCE;
    public static final ExecutorService dbWriterExecutor
            = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static final RoomDatabase.Callback sRoomDBCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    dbWriterExecutor.execute(() -> {
                        TaskDao taskDao = INSTANCE.taskDao();
                        taskDao.deleteAll();
                    });
                }
            };

    public static TaskDB getDB(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TaskDB.class, DB_NAME)
                            .addCallback(sRoomDBCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract TaskDao taskDao();
}
