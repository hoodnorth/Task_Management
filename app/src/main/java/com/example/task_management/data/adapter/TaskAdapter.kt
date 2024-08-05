package com.example.taskmanagement.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.task_management.R
import com.example.task_management.data.model.Task
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter(
    private var tasks: List<Task>,
    private val onClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task, onClick)
    }

    override fun getItemCount() = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: CardView = itemView.findViewById(R.id.cardView)
        private val taskName: TextView = itemView.findViewById(R.id.textViewTaskName)
        private val status: TextView = itemView.findViewById(R.id.textViewStatus)

        fun bind(task: Task, onClick: (Task) -> Unit) {
            taskName.text = task.taskName
            status.text = task.status

            // Set the color based on the deadline date and status
            val deadlineDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(task.deadlineDate)
            val currentDate = Calendar.getInstance().time
            val diffInDays = ((deadlineDate.time - currentDate.time) / (1000 * 60 * 60 * 24)).toInt()

            cardView.setCardBackgroundColor(
                when {
                    task.status == "Selesai" -> android.graphics.Color.BLUE
                    diffInDays <= 1 -> android.graphics.Color.RED
                    diffInDays <= 3 -> android.graphics.Color.YELLOW
                    diffInDays <= 5 -> android.graphics.Color.GREEN
                    else -> android.graphics.Color.WHITE // Default color if no condition matches
                }
            )

            itemView.setOnClickListener { onClick(task) }
        }
    }
}
