package com.example.crud_mad.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.crud_mad.model.TaskListModel

// DatabaseHelper class for managing SQLite database operations
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        // Database constants
        private const val DB_NAME = "task"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "tasklist"
        private const val ID = "id"
        private const val TASK_NAME = "taskname"
        private const val TASK_DETAILS = "taskdetails"
    }

    // Called when the database is created
    override fun onCreate(db: SQLiteDatabase?) {
        // SQL statement to create the table
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY,$TASK_NAME TEXT ,$TASK_DETAILS TEXT);"
        // Execute the SQL statement
        db?.execSQL(CREATE_TABLE)
    }

    // Called when the database needs to be upgraded
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop the existing table if it exists
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        // Recreate the table
        onCreate(db)
    }

    // Retrieve all tasks from the database
    @SuppressLint("Range")
    fun getAllTask(): List<TaskListModel> {
        val tasklist = ArrayList<TaskListModel>()
        // Get readable database
        val db: SQLiteDatabase = readableDatabase
        // SQL query to select all tasks
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        // Execute the query
        val cursor: Cursor = db.rawQuery(selectQuery, null)
        // Iterate through the cursor and add tasks to the list
        if (cursor.moveToFirst()) {
            do {
                val tasks = TaskListModel()
                tasks.id = cursor.getInt(cursor.getColumnIndex(ID))
                tasks.name = cursor.getString(cursor.getColumnIndex(TASK_NAME))
                tasks.details = cursor.getString(cursor.getColumnIndex(TASK_DETAILS))
                tasklist.add(tasks)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return tasklist
    }

    // Add a new task to the database
    fun addTask(tasks: TaskListModel): Boolean {
        // Get writable database
        val db = this.writableDatabase
        // Create ContentValues to store values
        val values = ContentValues()
        values.put(TASK_NAME, tasks.name)
        values.put(TASK_DETAILS, tasks.details)
        // Insert row into the table
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        // Return true if insertion successful, false otherwise
        return _success != -1L
    }

    // Retrieve a task by its ID from the database
    @SuppressLint("Range")
    fun getTask(_id: Int): TaskListModel? {
        val db = readableDatabase
        // SQL query to select a task by ID
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $ID = $_id"
        // Execute the query
        val cursor = db.rawQuery(selectQuery, null)
        var tasks: TaskListModel? = null
        // If the cursor has data, create a TaskListModel object
        if (cursor.moveToFirst()) {
            tasks = TaskListModel()
            tasks.id = cursor.getInt(cursor.getColumnIndex(ID))
            tasks.name = cursor.getString(cursor.getColumnIndex(TASK_NAME))
            tasks.details = cursor.getString(cursor.getColumnIndex(TASK_DETAILS))
        }
        cursor.close()
        return tasks
    }

    // Delete a task from the database by its ID
    fun deleteTask(_id: Int): Boolean {
        val db = this.writableDatabase
        // Delete row from the table where ID matches
        val _success = db.delete(TABLE_NAME, "$ID=?", arrayOf(_id.toString()))
        db.close()
        // Return true if deletion successful, false otherwise
        return _success != 0
    }

    // Update an existing task in the database
    fun updateTask(tasks: TaskListModel): Boolean {
        val db = this.writableDatabase
        // Create ContentValues to store updated values
        val values = ContentValues()
        values.put(TASK_NAME, tasks.name)
        values.put(TASK_DETAILS, tasks.details)
        // Update row in the table where ID matches
        val _success = db.update(TABLE_NAME, values, "$ID=?", arrayOf(tasks.id.toString()))
        db.close()
        // Return true if update successful, false otherwise
        return _success != 0
    }
}
