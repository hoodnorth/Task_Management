package com.example.task_management.ui

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task_management.R
import com.example.task_management.data.model.Task
import com.example.task_management.ui.view.TaskDetailFragment
import com.example.taskmanagement.ui.adapter.TaskAdapter
import com.example.taskmanagement.ui.view.AddTaskFragment
//import com.example.taskmanagement.ui.view.AddTaskFragment
//import com.example.taskmanagement.ui.viewmodel.TaskViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var taskAdapter: TaskAdapter

    private lateinit var progressBar: ProgressBar
    private lateinit var tasks: MutableList<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)

        tasks = mutableListOf()
        taskAdapter = TaskAdapter(tasks) { task ->
            openTaskDetailFragment(task)
        }

        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.adapter = taskAdapter

        fetchDataFromFirebase()

        fab = findViewById(R.id.fab)

        fab.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AddTaskFragment())
                .addToBackStack(null)
                .commit()
        }

    }

    private fun fetchDataFromFirebase() {
        progressBar.visibility = View.VISIBLE
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("tasks")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tasks.clear()
                for (dataSnapshot in snapshot.children) {
                    val task = dataSnapshot.getValue(Task::class.java)
                    if (task != null) {
                        tasks.add(task)
                    }
                }
                taskAdapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun openTaskDetailFragment(task: Task) {
        val dialogFragment = TaskDetailFragment.newInstance(task)
        dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogTheme)
        dialogFragment.show(supportFragmentManager, "TaskDetailDialog")
    }


}
