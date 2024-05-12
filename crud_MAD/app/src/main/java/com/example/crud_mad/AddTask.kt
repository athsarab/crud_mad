package com.example.crud_mad

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.crud_mad.database.DatabaseHelper
import com.example.crud_mad.model.TaskListModel

class AddTask : AppCompatActivity() {

    private lateinit var btn_del: Button
    private lateinit var btn_save: Button
    private lateinit var et_name: EditText
    private lateinit var et_details: EditText
    private var dbHandler: DatabaseHelper? = null
    private var isEditMode: Boolean = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task) // This line was missing

        btn_save = findViewById(R.id.btn_save)
        btn_del = findViewById(R.id.btn_del)
        et_name = findViewById(R.id.name)
        et_details = findViewById(R.id.details)

        dbHandler = DatabaseHelper(this)

        if (intent != null && intent.getStringExtra("Mode") == "E") {
            // Update Data
            isEditMode = true
            btn_save.text = "Update Data"
            btn_del.visibility = View.VISIBLE

            val tasks: TaskListModel? = dbHandler!!.getTask(intent.getIntExtra("Id", 0))
            tasks?.let {
                et_name.setText(it.name)
                et_details.setText(it.details)
            }
        } else {
            // Insert new data
            isEditMode = false
            btn_save.text = "Save data"
            btn_del.visibility = View.GONE
        }

        btn_save.setOnClickListener {
            var success: Boolean = false
            val tasks = TaskListModel()

            if (isEditMode) {
                // Update
                tasks.id = intent.getIntExtra("Id", 0)
                tasks.name = et_name.text.toString()
                tasks.details = et_details.text.toString()
                success = dbHandler?.updateTask(tasks) ?: false
            } else {
                // Insert
                tasks.name = et_name.text.toString()
                tasks.details = et_details.text.toString()
                success = dbHandler?.addTask(tasks) ?: false
            }

            if (success) {
                val i = Intent(applicationContext, MainActivity::class.java)
                startActivity(i)
                finish()
            } else {
                Toast.makeText(applicationContext, "Something went Wrong!!", Toast.LENGTH_LONG).show()
            }
        }

        btn_del.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                .setTitle("Info")
                .setMessage("Click Yes If You Want to Delete task")
                .setPositiveButton("YES") { dialog, _ ->
                    val success = dbHandler?.deleteTask(intent.getIntExtra("Id", 0)) ?: false
                    if (success) {
                        finish()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            dialog.show()
        }
    }
}
