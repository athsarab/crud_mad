package com.example.crud_mad.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.crud_mad.model.TaskListModel

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "task"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "tasklist"
        private const val ID = "id"
        private const val TASK_NAME = "taskname"
        private const val TASK_DETAILS = "taskdetails"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY,$TASK_NAME TEXT ,$TASK_DETAILS TEXT);"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    @SuppressLint("Range")
    fun getAllTask(): List<TaskListModel> {
        val tasklist = ArrayList<TaskListModel>()
        val db: SQLiteDatabase = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor: Cursor = db.rawQuery(selectQuery, null)
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

    fun addTask(tasks: TaskListModel): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(TASK_NAME, tasks.name)
        values.put(TASK_DETAILS, tasks.details)
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        return _success != -1L
    }

    @SuppressLint("Range")
    fun getTask(_id: Int): TaskListModel? {
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)
        var tasks: TaskListModel? = null
        if (cursor.moveToFirst()) {
            tasks = TaskListModel()
            tasks.id = cursor.getInt(cursor.getColumnIndex(ID))
            tasks.name = cursor.getString(cursor.getColumnIndex(TASK_NAME))
            tasks.details = cursor.getString(cursor.getColumnIndex(TASK_DETAILS))
        }
        cursor.close()
        return tasks
    }

    fun deleteTask(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, "$ID=?", arrayOf(_id.toString()))
        db.close()
        return _success != 0
    }

    fun updateTask(tasks: TaskListModel): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(TASK_NAME, tasks.name)
        values.put(TASK_DETAILS, tasks.details)
        val _success = db.update(TABLE_NAME, values, "$ID=?", arrayOf(tasks.id.toString()))
        db.close()
        return _success != 0
    }
}
