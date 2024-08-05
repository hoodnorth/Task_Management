package com.example.taskmanagement.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.task_management.data.model.Task
import com.google.firebase.database.*

class TaskRepository {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("tasks")

    fun saveTask(task: Task) {
        val taskId = database.push().key ?: return
        val taskWithId = task.copy(id = taskId) // Create a new Task objectwith the ID
        database.child(taskId).setValue(taskWithId)
    }

}
