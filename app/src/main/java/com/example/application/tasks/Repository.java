package com.example.application.tasks;

import android.app.Application;
import android.app.TaskStackBuilder;
import android.os.AsyncTask;
import android.widget.RelativeLayout;

import androidx.lifecycle.LiveData;

import java.util.List;

public class Repository {

    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    public Repository(Application application){
        TaskDatabase database = TaskDatabase.getInstance(application);
        this.taskDao = database.taskDao();
        this.allTasks = taskDao.getAllTasks();
    }

    public void insert(Task task){
        new InsertAsyncTask(taskDao).execute(task);
    }

    public void delete(Task task){
        new DeleteAsyncTask(taskDao).execute(task);
    }

    public void update(Task task){
        new UpdateAsyncTask(taskDao).execute(task);
    }

    public void deleteAll(){
        new DeleteAllAsyncTask(taskDao).execute();
    }

    public LiveData<List<Task>> getAllTasks(){
        return allTasks; // done automatically on the background thread
    }


    ////
    private static class InsertAsyncTask extends android.os.AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private InsertAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insert(tasks[0]);
            return null;
        }
    }


    private static class UpdateAsyncTask extends android.os.AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private UpdateAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.update(tasks[0]);
            return null;
        }
    }


    private static class DeleteAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private DeleteAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.delete(tasks[0]);
            return null;
        }
    }


    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private TaskDao taskDao;

        private DeleteAllAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.deleteAll();
            return null;
        }
    }



}
