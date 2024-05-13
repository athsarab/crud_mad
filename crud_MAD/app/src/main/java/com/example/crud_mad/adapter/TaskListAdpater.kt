package com.example.crud_mad.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.crud_mad.AddTask
import com.example.crud_mad.R
import com.example.crud_mad.model.TaskListModel

// Adapter class for the RecyclerView to display a list of tasks
class TaskListAdpater(tasklist: List<TaskListModel>, internal var context: Context) :
    RecyclerView.Adapter<TaskListAdpater.TaskViewHolder>() {

    // List of tasks
    internal var tasklist: List<TaskListModel> = ArrayList()

    init {
        // Initialize the list of tasks
        this.tasklist = tasklist
    }

    // View holder class to hold the views for each item in the RecyclerView
    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.txt_name) // TextView for task name
        var details: TextView = view.findViewById(R.id.txt_details) // TextView for task details
        var btn_edit: Button = view.findViewById(R.id.btn_edit) // Button to edit task
    }

    // Called when RecyclerView needs a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // Inflate the layout for each item in the RecyclerView
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_task_list, parent, false)
        return TaskViewHolder(view)
    }

    // Called by RecyclerView to display the data at the specified position
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val tasks = tasklist[position]
        holder.name.text = tasks.name // Set the task name
        holder.details.text = tasks.details // Set the task details
        holder.btn_edit.setOnClickListener {
            // Click listener for the edit button
            val i = Intent(context, AddTask::class.java)
            i.putExtra("Mode", "E") // Set the mode to edit
            i.putExtra("Id", tasks.id) // Pass the task ID
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK // Flag to start a new activity
            context.startActivity(i) // Start the AddTask activity
        }
    }

    // Return the size of the task list
    override fun getItemCount(): Int {
        return tasklist.size
    }
}
