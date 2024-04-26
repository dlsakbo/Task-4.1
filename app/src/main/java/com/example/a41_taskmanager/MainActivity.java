package com.example.a41_taskmanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Task> taskList;
    ImageView addTaskImageView;
    SQLiteDatabase database;
    MySQLiteHelper mySQLiteHelper;
    TaskListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySQLiteHelper = new MySQLiteHelper(this);
        database = mySQLiteHelper.getWritableDatabase();
        recyclerView = findViewById(R.id.rv);
        addTaskImageView = findViewById(R.id.btn_add_task);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskList = new ArrayList<>();
        adapter = new TaskListAdapter(this, taskList);
        fetchDataFromDB();
        addTaskImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditActivity.class);
                startActivity(i);
            }
        });
        findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchDataFromDB();
            }
        });
    }

    public void fetchDataFromDB() {
        taskList.clear();
        Cursor cursor = database.query(
                "tasks",
                null,
                null,
                null,
                null,
                null,
                "dueDate DESC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                taskList.add(new Task(
                        cursor.getLong(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("title")),
                        cursor.getString(cursor.getColumnIndex("description")),
                        cursor.getString(cursor.getColumnIndex("dueDate"))
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }
        recyclerView.setAdapter(adapter);
    }

    private class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {
        private List<Task> dataList;
        private Context context;

        public TaskListAdapter(Context context, List<Task> taskList) {
            this.context = context;
            this.dataList = taskList;
        }

        @Override
        public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent, false);
            return new TaskViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TaskViewHolder holder, int position) {
            Task task = dataList.get(position);
            holder.textView.setText(task.title);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public class TaskViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;

            public TaskViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.tv_task_name);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, ViewTaskActivity.class);
                        i.putExtra("task", dataList.get(getAdapterPosition()));
                        context.startActivity(i);
                    }
                });
            }
        }
    }
}
