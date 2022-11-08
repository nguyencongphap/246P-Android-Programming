package com.example.todolistapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {

    public TaskAdapter(@NonNull Context context, int resource, @NonNull List<Task> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data object for this position
        Task task = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_list_item, parent, false);
        }
        // Lookup view for data population
        TextView taskItemTitle = (TextView) convertView.findViewById(R.id.taskItemTitle);
        TextView taskItemStatus = (TextView) convertView.findViewById(R.id.taskItemStatus);

        // Populate the data into the template view using the data object
        taskItemTitle.setText(task.getTitle());
        taskItemStatus.setText(task.getStatus());

        // Return the completed view to render on screen
        return convertView;
    }
}
