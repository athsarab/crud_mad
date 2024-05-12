package com.example.crud_mad

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.crud_mad.adapter.TaskListAdpater
import com.example.crud_mad.database.DatabaseHelper

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerTask: RecyclerView
    private lateinit var btnAdd: Button
    private var taskListAdapter: TaskListAdpater? = null
    private var dbHandler: DatabaseHelper? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerTask = findViewById(R.id.rv_list)
        btnAdd = findViewById(R.id.bt_add_item)

        dbHandler = DatabaseHelper(this)
        fetchList()

        btnAdd.setOnClickListener {
            val i = Intent(applicationContext, AddTask::class.java)
            startActivity(i)
        }
    }

    private fun fetchList() {
        val taskList = dbHandler?.getAllTask() ?: ArrayList()
        taskListAdapter = TaskListAdpater(taskList, applicationContext)
        recyclerTask.layoutManager = LinearLayoutManager(applicationContext)
        recyclerTask.adapter = taskListAdapter
        taskListAdapter?.notifyDataSetChanged()
    }
}
