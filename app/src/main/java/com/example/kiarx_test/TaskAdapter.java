package com.example.kiarx_test;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

// View Layer: Handles the display of the list of tasks with headers
public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ListItem> listItems;
    private final OnTaskActionListener listener;

    public interface OnTaskActionListener {
        void onTaskClick(Task task);
        void onDeleteClick(Task task);
        void onTaskStatusChanged(Task task, boolean isCompleted);
    }

    public TaskAdapter(List<Task> taskList, OnTaskActionListener listener) {
        this.listener = listener;
        this.listItems = groupTasksByDate(taskList);
    }

    private List<ListItem> groupTasksByDate(List<Task> tasks) {
        // Let's use a simpler approach: sort tasks by added time descending first
        Collections.sort(tasks, (t1, t2) -> Long.compare(t2.getAddedTime(), t1.getAddedTime()));

        List<ListItem> items = new ArrayList<>();
        SimpleDateFormat headerFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        SimpleDateFormat displayFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault());
        String today = headerFormat.format(new Date());

        String lastHeader = "";

        for (Task task : tasks) {
            String taskDateKey = headerFormat.format(new Date(task.getAddedTime()));
            String displayHeader;

            if (taskDateKey.equals(today)) {
                displayHeader = "Today";
            } else {
                displayHeader = displayFormat.format(new Date(task.getAddedTime()));
            }

            if (!displayHeader.equals(lastHeader)) {
                items.add(new HeaderItem(displayHeader));
                lastHeader = displayHeader;
            }
            items.add(new TaskItem(task));
        }
        return items;
    }

    public void updateTasks(List<Task> newTaskList) {
        this.listItems = groupTasksByDate(newTaskList);
        notifyDataSetChanged();
    }
    
    // Helper method to get task from position for swipe gestures
    public Task getTaskAt(int position) {
        if (position >= 0 && position < listItems.size()) {
            ListItem item = listItems.get(position);
            if (item.getType() == ListItem.TYPE_TASK) {
                return ((TaskItem) item).getTask();
            }
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return listItems.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ListItem.TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
            return new TaskViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind((HeaderItem) listItems.get(position));
        } else if (holder instanceof TaskViewHolder) {
            ((TaskViewHolder) holder).bind(((TaskItem) listItems.get(position)).getTask());
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView textHeader;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            textHeader = itemView.findViewById(R.id.textHeader);
        }

        void bind(HeaderItem item) {
            textHeader.setText(item.getDate());
        }
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDetails;
        TextView textViewTimestamp;
        CheckBox checkBoxCompleted;
        // Removed delete button reference

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDetails = itemView.findViewById(R.id.textViewDetails);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
            checkBoxCompleted = itemView.findViewById(R.id.checkBoxCompleted);
        }

        public void bind(final Task task) {
            textViewTitle.setText(task.getTitle());
            
            if (task.getDetails() == null || task.getDetails().trim().isEmpty()) {
                textViewDetails.setVisibility(View.GONE);
            } else {
                textViewDetails.setVisibility(View.VISIBLE);
                textViewDetails.setText(task.getDetails());
            }

            // Just show time for tasks, as date is in header
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            String timeString;
            
            if (task.isCompleted()) {
                 if (task.getCompletedTime() > 0) {
                    timeString = "Completed at " + timeFormat.format(new Date(task.getCompletedTime()));
                } else {
                    timeString = "Completed";
                }
            } else {
                timeString = "Added at " + timeFormat.format(new Date(task.getAddedTime()));
            }
            textViewTimestamp.setText(timeString);


            checkBoxCompleted.setOnCheckedChangeListener(null); 
            checkBoxCompleted.setChecked(task.isCompleted());

            updateStrikeThrough(task.isCompleted());

            itemView.setOnClickListener(v -> listener.onTaskClick(task));

            // Removed click listener for delete button since it's removed

            checkBoxCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
                task.setCompleted(isChecked);
                if (isChecked) {
                    task.setCompletedTime(System.currentTimeMillis());
                } else {
                    task.setCompletedTime(0);
                }
                
                // We notify the listener which updates DB and likely calls updateTasks, 
                // refreshing the whole list which handles moving/updating UI logic correctly.
                listener.onTaskStatusChanged(task, isChecked);
            });
        }

        private void updateStrikeThrough(boolean isCompleted) {
            if (isCompleted) {
                textViewTitle.setPaintFlags(textViewTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                textViewTitle.setAlpha(0.5f);
                textViewDetails.setAlpha(0.5f);
            } else {
                textViewTitle.setPaintFlags(textViewTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                textViewTitle.setAlpha(1.0f);
                textViewDetails.setAlpha(1.0f);
            }
        }
    }
}
