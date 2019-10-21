package com.example.application.tasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> allTasks = new ArrayList<>();
    private ListItemClickListener listItemClickListener;

    public TaskAdapter(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    public void setTasks(List<Task> tasks) {
        this.allTasks = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return allTasks.size();
    }

    public Task getTaskAt(int position) {
        return allTasks.get(position);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {

        private TextView priorityTV;
        private TextView titleTV;
        private TextView descriptionTV;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            priorityTV = itemView.findViewById(R.id.text_view_priority);
            titleTV = itemView.findViewById(R.id.text_view_title);
            descriptionTV = itemView.findViewById(R.id.text_view_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listItemClickListener.onListItemClick(allTasks.get(getAdapterPosition()));
                }
            });

        }

        public void onBind(int position) {
            Task currentTask = allTasks.get(position);
            priorityTV.setText(String.valueOf(currentTask.getPriority()));
            titleTV.setText(currentTask.getTitle());
            descriptionTV.setText(currentTask.getDescription());
        }
    }

}
