package com.example.kiarx_test;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

// Controller Layer: Manages the interaction between the Model and View
public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskActionListener {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private DatabaseHelper databaseHelper;
    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);
            
            // Handle window insets for edge-to-edge display
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Initialize Database Helper
            databaseHelper = new DatabaseHelper(this);
            
            // Fetch initial data
            try {
                taskList = databaseHelper.getAllTasks();
            } catch (Exception e) {
                Log.e("MainActivity", "Error fetching tasks", e);
                taskList = new ArrayList<>();
                Toast.makeText(this, "Error loading tasks", Toast.LENGTH_LONG).show();
            }

            // Setup RecyclerView
            recyclerView = findViewById(R.id.recyclerViewTasks);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            taskAdapter = new TaskAdapter(taskList, this);
            recyclerView.setAdapter(taskAdapter);

            // Setup ItemTouchHelper for swipe gestures
            setupSwipeGestures();

            // Setup Floating Action Button for adding tasks
            FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);
            fabAddTask.setOnClickListener(v -> showAddEditTaskDialog(null));
            
        } catch (Exception e) {
            Log.e("MainActivity", "Error in onCreate", e);
            Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setupSwipeGestures() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof TaskAdapter.HeaderViewHolder) {
                    return 0; // Disable swipe for headers
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (viewHolder instanceof TaskAdapter.HeaderViewHolder) return;

                int position = viewHolder.getAdapterPosition();
                Task task = taskAdapter.getTaskAt(position);

                if (task == null) return;

                if (direction == ItemTouchHelper.LEFT) {
                    // Swipe Left to Delete
                    onDeleteClick(task);
                    taskAdapter.notifyItemChanged(position);
                } else {
                    // Swipe Right to Edit
                    showAddEditTaskDialog(task);
                    taskAdapter.notifyItemChanged(position); // Reset swipe state
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (viewHolder instanceof TaskAdapter.HeaderViewHolder) return;

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                
                View itemView = viewHolder.itemView;
                int backgroundCornerOffset = 20;

                if (dX > 0) { // Swipe Right (Edit)
                    // Use a softer color for Edit (e.g., Teal/Green shade)
                    int color = Color.parseColor("#009688"); 
                    Drawable icon = ContextCompat.getDrawable(MainActivity.this, android.R.drawable.ic_menu_edit);
                    // Tint icon white for better contrast
                    if (icon != null) {
                        icon.setTint(Color.WHITE);
                    }
                    ColorDrawable background = new ColorDrawable(color); 
                    
                    if (icon != null) {
                        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                        int iconBottom = iconTop + icon.getIntrinsicHeight();

                        int iconLeft = itemView.getLeft() + iconMargin;
                        int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                        background.setBounds(itemView.getLeft(), itemView.getTop(),
                                itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
                        
                        background.draw(c);
                        icon.draw(c);
                    }
                } else if (dX < 0) { // Swipe Left (Delete)
                    // Use a softer red for Delete
                    int color = Color.parseColor("#EF5350");
                    Drawable icon = ContextCompat.getDrawable(MainActivity.this, android.R.drawable.ic_menu_delete);
                    if (icon != null) {
                        icon.setTint(Color.WHITE);
                    }
                    ColorDrawable background = new ColorDrawable(color);
                    
                    if (icon != null) {
                        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                        int iconBottom = iconTop + icon.getIntrinsicHeight();

                        int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                        int iconRight = itemView.getRight() - iconMargin;
                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                        background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                                itemView.getTop(), itemView.getRight(), itemView.getBottom());
                        
                        background.draw(c);
                        icon.draw(c);
                    }
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    // Displays a dialog to add a new task or edit an existing one
    private void showAddEditTaskDialog(Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_edit_task, null);
        builder.setView(dialogView);

        final EditText editTextTitle = dialogView.findViewById(R.id.editTextTitle);
        final EditText editTextDetails = dialogView.findViewById(R.id.editTextDetails);

        if (task != null) {
            editTextTitle.setText(task.getTitle());
            editTextDetails.setText(task.getDetails());
            builder.setTitle("Edit Task");
        } else {
            builder.setTitle("Add Task");
        }

        builder.setPositiveButton("Save", (dialog, which) -> {
            String title = editTextTitle.getText().toString().trim();
            String details = editTextDetails.getText().toString().trim();

            if (!title.isEmpty()) {
                if (task != null) {
                    // Update existing task
                    task.setTitle(title);
                    task.setDetails(details);
                    databaseHelper.updateTask(task);
                } else {
                    // Create new task
                    Task newTask = new Task(title, details, false, System.currentTimeMillis());
                    databaseHelper.addTask(newTask);
                }
                refreshTaskList();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    // Refreshes the task list from the database and updates the adapter
    private void refreshTaskList() {
        taskList = databaseHelper.getAllTasks();
        taskAdapter.updateTasks(taskList);
    }

    @Override
    public void onTaskClick(Task task) {
        showAddEditTaskDialog(task); // Edit task on click
    }

    @Override
    public void onDeleteClick(Task task) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    databaseHelper.deleteTask(task.getId());
                    refreshTaskList();
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onTaskStatusChanged(Task task, boolean isCompleted) {
        task.setCompleted(isCompleted);
        databaseHelper.updateTask(task);
        refreshTaskList();
    }
}
