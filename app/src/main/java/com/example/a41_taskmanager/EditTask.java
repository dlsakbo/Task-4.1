package com.example.a41_taskmanager;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity {
    TextView heading;
    EditText title, description;
    SQLiteDatabase database;

    MySQLiteHelper mySQLiteHelper;
    Task task = null;

    String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        mySQLiteHelper = new MySQLiteHelper(this);
        database = mySQLiteHelper.getWritableDatabase();
        heading = findViewById(R.id.heading);
        title = findViewById(R.id.et_title);
        description = findViewById(R.id.et_description);
        Intent intent = getIntent();
        task = (Task) intent.getSerializableExtra("task");

        if (task != null) {
            heading.setText("Editing the Task: " + task.id);
            title.setText(task.title);
            description.setText(task.description);
            date = task.dueDate;
            Button createOrUpdateButton = findViewById(R.id.btnAddTaskToDb);
            createOrUpdateButton.setText("Update");
        }

        findViewById(R.id.btnAddTaskToDb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDataToDB();
            }
        });

        findViewById(R.id.buttonPickDateTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int sy, int sm, int sd) {
                        date = sd + "/" + sm + "/" + sy;
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void addDataToDB() {
        String titleText = title.getText().toString();
        String descText = description.getText().toString();
        if (titleText.isEmpty() || descText.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues values = new ContentValues();
        values.put("title", titleText);
        values.put("description", descText);
        values.put("dueDate", date);
        if (task == null) {
            database.insert("tasks", null, values);
            Toast.makeText(this, "Task created", Toast.LENGTH_SHORT).show();
        } else {
            database.update("tasks", values, "id = ?", new String[]{String.valueOf(task.id)});
            Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
