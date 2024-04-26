package com.example.a41_taskmanager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ViewTaskActivity extends AppCompatActivity {

    private TextView titleTextView, descriptionTextView, dueDateTextView, taskIdTextView;

    private MySQLiteHelper mySQLiteHelper;
    private SQLiteDatabase database;

    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_task);
        Intent intent = getIntent();
        task = (Task) intent.getSerializableExtra("task");
        mySQLiteHelper = new MySQLiteHelper(this);
        database = mySQLiteHelper.getWritableDatabase();
        titleTextView = findViewById(R.id.tv_title);
        descriptionTextView = findViewById(R.id.tv_description);
        taskIdTextView = findViewById(R.id.id);
        dueDateTextView = findViewById(R.id.tv_due_date);

        taskIdTextView.setText("Viewing Task: " + task.id);
        titleTextView.setText("Title: " + task.title);
        descriptionTextView.setText(task.description);
        dueDateTextView.setText("Task Due on: " + task.dueDate);
        findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditActivity.class);
                i.putExtra("task", task);
                startActivity(i);
            }
        });

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.delete("tasks", "id = ?", new String[]{String.valueOf(task.id)});
                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
