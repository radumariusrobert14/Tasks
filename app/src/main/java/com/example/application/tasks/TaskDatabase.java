package com.example.application.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Task.class}, version = 1)
public abstract class TaskDatabase extends RoomDatabase {

    private static TaskDatabase instance; //singleton

    public abstract TaskDao taskDao();

    public static synchronized TaskDatabase getInstance(Context context){
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),TaskDatabase.class,"task_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new InsertByDefault(instance).execute();
        }
    };

    private static class InsertByDefault extends AsyncTask<Void, Void, Void> {
        private TaskDao taskDao;

        private InsertByDefault(TaskDatabase taskDatabase){
            this.taskDao = taskDatabase.taskDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.insert(new Task("Title Example", "Description Example", 3));
            taskDao.insert(new Task("Title Example", "Description Example", 4));
            taskDao.insert(new Task("Title Example", "Description Example", 1));
            return null;
        }
    }

}
