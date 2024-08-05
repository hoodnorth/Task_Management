package com.example.taskmanagement.ui.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.task_management.R
import com.example.task_management.data.model.Task
import com.example.task_management.ui.MainActivity
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTaskFragment : Fragment() {

    private lateinit var editTextTaskName: EditText
    private lateinit var editTextStartDate: EditText
    private lateinit var editTextDeadlineDate: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var spinnerPriority: Spinner
    private lateinit var spinnerStatus: Spinner
    private lateinit var spinnerCategory: Spinner
    private lateinit var editTextAdditionalNotes: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonCancel: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_task, container, false)

        editTextTaskName = view.findViewById(R.id.editTextTaskName)
        editTextStartDate = view.findViewById(R.id.editTextStartDate)
        editTextDeadlineDate = view.findViewById(R.id.editTextDeadlineDate)
        editTextDescription = view.findViewById(R.id.editTextDescription)
        spinnerPriority = view.findViewById(R.id.spinnerPriority)
        spinnerStatus = view.findViewById(R.id.spinnerStatus)
        spinnerCategory = view.findViewById(R.id.spinnerCategory)
        editTextAdditionalNotes = view.findViewById(R.id.editTextAdditionalNotes)
        buttonSave = view.findViewById(R.id.buttonSave)
        buttonCancel = view.findViewById(R.id.buttonCancel)
        progressBar = view.findViewById(R.id.progressBar)

        progressBar.visibility = View.GONE

        val spinners = listOf(
            R.id.spinnerCategory to R.array.category_options,
            R.id.spinnerStatus to R.array.status_options,
            R.id.spinnerPriority to R.array.priority_options
        )

        spinners.forEach { (spinnerId, arrayId) ->
            val spinner: Spinner = view.findViewById(spinnerId)
            ArrayAdapter.createFromResource(
                requireContext(),
                arrayId,
                R.layout.item_spinner
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        }

        // Set up date pickers
        setDatePicker(editTextStartDate)
        setDatePicker(editTextDeadlineDate)

        buttonSave.setOnClickListener {
            saveTaskToFirebase()
        }

        buttonCancel.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }

    private fun setDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            editText.setText(dateFormat.format(calendar.time))
        }

        editText.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun saveTaskToFirebase() {
        val taskName = editTextTaskName.text.toString().trim()
        val startDate = editTextStartDate.text.toString().trim()
        val deadlineDate = editTextDeadlineDate.text.toString().trim()
        val description = editTextDescription.text.toString().trim()
        val priority = spinnerPriority.selectedItem.toString()
        val status = spinnerStatus.selectedItem.toString()
        val category = spinnerCategory.selectedItem.toString()
        val additionalNotes = editTextAdditionalNotes.text.toString().trim()

        if (taskName.isEmpty() || startDate.isEmpty() || deadlineDate.isEmpty() || description.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("tasks")

        val taskId = myRef.push().key
        if (taskId != null) {
            val task = Task(taskId, taskName, startDate, deadlineDate, description, priority, status, category, additionalNotes)
            myRef.child(taskId).setValue(task).addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Task saved successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(requireContext(), "Failed to save task", Toast.LENGTH_SHORT).show()
                    // Handle failed save
                }
            }
        }
    }
}
