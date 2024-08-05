package com.example.task_management.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.example.task_management.R
import com.example.task_management.data.model.Task
import com.google.firebase.database.FirebaseDatabase

class TaskDetailFragment : DialogFragment() {

    private lateinit var editTextTaskName: EditText
    private lateinit var spinnerStatus: Spinner
    private lateinit var spinnerPriority: Spinner
    private lateinit var spinnerCategory: Spinner
    private lateinit var editTextStartDate: EditText
    private lateinit var editTextDeadlineDate: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var editTextAdditionalNotes: EditText
    private lateinit var buttonEdit: TextView
    private lateinit var buttonSave: TextView
    private lateinit var buttonDelete: TextView
    private lateinit var buttonClose: TextView

    private var isEditing = false
    private lateinit var currentTaskId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_detail, container, false)

        // Initialize UI elements
        editTextTaskName = view.findViewById(R.id.editTextTaskName)
        spinnerStatus = view.findViewById(R.id.spinnerStatus)
        spinnerPriority = view.findViewById(R.id.spinnerPriority)
        spinnerCategory = view.findViewById(R.id.spinnerCategory)
        editTextStartDate = view.findViewById(R.id.editTextStartDate)
        editTextDeadlineDate = view.findViewById(R.id.editTextDeadlineDate)
        editTextDescription = view.findViewById(R.id.editTextDescription)
        editTextAdditionalNotes = view.findViewById(R.id.editTextAdditionalNotes)
        buttonEdit = view.findViewById(R.id.buttonEdit)
        buttonSave = view.findViewById(R.id.buttonSave)
        buttonDelete = view.findViewById(R.id.buttonDelete)
        buttonClose = view.findViewById(R.id.buttonClose)

        // Call setupSpinners to initialize the spinner adapters
        setupSpinners()

        // Retrieve and set data
        val task = arguments?.getParcelable<Task>("task")
        task?.let {
            currentTaskId = it.id
            editTextTaskName.setText(it.taskName)
            editTextStartDate.setText(it.startDate)
            editTextDeadlineDate.setText(it.deadlineDate)
            editTextDescription.setText(it.description)
            spinnerPriority.setSelection(getSpinnerIndex(spinnerPriority, it.priority))
            spinnerStatus.setSelection(getSpinnerIndex(spinnerStatus, it.status))
            spinnerCategory.setSelection(getSpinnerIndex(spinnerCategory, it.category))
            editTextAdditionalNotes.setText(it.additionalNotes)
        }



        // Set up button listeners
        buttonEdit.setOnClickListener {
            if (isEditing) {
                enableEditing(false)
                buttonSave.visibility = View.GONE
                buttonEdit.text = "Edit"
                saveTask()
            } else {
                enableEditing(true)
                buttonSave.visibility = View.VISIBLE
                buttonEdit.text = "Cancel"
            }
            isEditing = !isEditing
        }

        buttonSave.setOnClickListener {
            saveTask()
        }

        buttonDelete.setOnClickListener {
            deleteTask()
        }

        buttonClose.setOnClickListener {
            dismiss() // Close the dialog
        }

        return view
    }

    private fun enableEditing(enable: Boolean) {
        editTextTaskName.isEnabled = enable
        editTextStartDate.isEnabled = enable
        editTextDeadlineDate.isEnabled = enable
        editTextDescription.isEnabled = enable
        spinnerStatus.isEnabled = enable
        spinnerPriority.isEnabled = enable
        spinnerCategory.isEnabled = enable
        editTextAdditionalNotes.isEnabled = enable
    }

    private fun saveTask() {
        val taskName = editTextTaskName.text.toString().trim()
        val startDate = editTextStartDate.text.toString().trim()
        val deadlineDate = editTextDeadlineDate.text.toString().trim()
        val description = editTextDescription.text.toString().trim()
        val priority = spinnerPriority.selectedItem.toString()
        val status = spinnerStatus.selectedItem.toString()
        val category = spinnerCategory.selectedItem.toString()
        val additionalNotes = editTextAdditionalNotes.text.toString().trim()

        if (taskName.isEmpty() || startDate.isEmpty() || deadlineDate.isEmpty() || description.isEmpty()) {
            Toast.makeText(context, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        val database = FirebaseDatabase.getInstance().reference
        val taskUpdates = mapOf(
            "taskName" to taskName,
            "startDate" to startDate,
            "deadlineDate" to deadlineDate,
            "description" to description,
            "status" to status,
            "priority" to priority,
            "category" to category,
            "additionalNotes" to additionalNotes
        )

        database.child("tasks").child(currentTaskId).updateChildren(taskUpdates)
            .addOnSuccessListener {
                Toast.makeText(context, "Task updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to update task", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteTask() {
        val database = FirebaseDatabase.getInstance().reference
        database.child("tasks").child(currentTaskId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Task deleted successfully", Toast.LENGTH_SHORT).show()
                dismiss()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to delete task", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupSpinners() {

        val priorityAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.priority_options,
            android.R.layout.simple_spinner_item
        )
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPriority.adapter = priorityAdapter

        val statusAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.status_options,
            android.R.layout.simple_spinner_item
        )
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = statusAdapter

        val categoryAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.category_options,
            android.R.layout.simple_spinner_item
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = categoryAdapter
    }

    private fun getSpinnerIndex(spinner: Spinner, value: String): Int {
        val adapter = spinner.adapter as ArrayAdapter<*>
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i).toString() == value) {
                return i
            }
        }
        return 0 // atau return -1 jika tidak ditemukan
    }

    companion object {
        fun newInstance(task: Task): TaskDetailFragment {
            val fragment = TaskDetailFragment()
            val args = Bundle()
            args.putParcelable("task", task)
            fragment.arguments = args
            return fragment
        }
    }
}
