package com.example.application.tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditTaskActivity extends AppCompatActivity {

    private EditText title;
    private EditText description;
    private NumberPicker priority;

    public static final String EXTRA_TITLE =
            "com.example.application.tasks.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.example.application.tasks.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY =
            "com.example.application.tasks.EXTRA_PRIORITY";
    public static final String EXTRA_ID =
            "com.example.application.tasks.EXTRA_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        title = findViewById(R.id.edit_text_title);
        description = findViewById(R.id.edit_text_description);
        priority = findViewById(R.id.number_picker_priority);

        priority.setMinValue(1);
        priority.setMaxValue(5);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Task");
            title.setText(intent.getStringExtra(EXTRA_TITLE));
            description.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            priority.setValue(intent.getIntExtra(EXTRA_PRIORITY,1));
        } else {
            setTitle("Add Task");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_task_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemSelectedId = item.getItemId();
        switch (itemSelectedId) {
            case R.id.save_menu_item:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote(){
        String noteTitle = title.getText().toString();
        String noteDescription = description.getText().toString();
        int notePriority = priority.getValue();

        // verify
        if (noteTitle.trim().isEmpty() || noteDescription.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please insert valid data for title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, noteTitle);
        data.putExtra(EXTRA_DESCRIPTION, noteDescription);
        data.putExtra(EXTRA_PRIORITY, notePriority);

        int noteId = getIntent().getIntExtra(EXTRA_ID, -1);

        if (noteId != -1) {
            data.putExtra(EXTRA_ID, noteId);
        }

        setResult(RESULT_OK, data);
        finish();

    }
}
