package com.example.application.tasks;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM tasks_table")
    void deleteAll();

    @Query("SELECT * FROM tasks_table ORDER BY priority DESC")
    LiveData<List<Task>> getAllTasks();

}
