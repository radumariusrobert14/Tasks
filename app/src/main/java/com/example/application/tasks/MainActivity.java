package com.example.application.tasks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewModel viewModel;
    private FloatingActionButton fab;

    public static final int ADD_TASK_REQUEST_CODE = 1;
    public static final int EDIT_TASK_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final TaskAdapter adapter = new TaskAdapter(new ListItemClickListener() {
            @Override
            public void onListItemClick(Task task) {
                // update
                Intent i = new Intent(MainActivity.this, AddEditTaskActivity.class);
                i.putExtra(AddEditTaskActivity.EXTRA_TITLE, task.getTitle());
                i.putExtra(AddEditTaskActivity.EXTRA_DESCRIPTION, task.getDescription());
                i.putExtra(AddEditTaskActivity.EXTRA_PRIORITY, task.getPriority());
                i.putExtra(AddEditTaskActivity.EXTRA_ID, task.getId());
                startActivityForResult(i, EDIT_TASK_REQUEST_CODE);
            }
        });

        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.setTasks(tasks);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.delete(adapter.getTaskAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

                fab = findViewById(R.id.button_add_task);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddEditTaskActivity.class);
                startActivityForResult(i, ADD_TASK_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditTaskActivity.EXTRA_PRIORITY, 1);

            Task newTask = new Task(title, description, priority);
            viewModel.insert(newTask);
            Toast.makeText(MainActivity.this, "Task saved", Toast.LENGTH_SHORT).show();

        } else if (requestCode == EDIT_TASK_REQUEST_CODE && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditTaskActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(MainActivity.this, "Task can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditTaskActivity.EXTRA_PRIORITY, 1);

            Task task = new Task(title, description, priority);
            task.setId(id);

            viewModel.update(task);
            Toast.makeText(MainActivity.this, "Task updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Task not saved", Toast.LENGTH_SHORT).show();
        }
    }

    // menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete_all_tasks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int currentMenuItemSelected = item.getItemId();
        switch (currentMenuItemSelected) {
            case R.id.delete_all_menu_item:
                viewModel.deleteAll();
                Toast.makeText(MainActivity.this, "All tasks deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
