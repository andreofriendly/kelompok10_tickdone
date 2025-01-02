package com.example.kelompok10_tickdone

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelompok10_tickdone.adapter.RvTasksAdapter
import com.example.kelompok10_tickdone.databinding.FragmentCalenderBinding
import com.example.kelompok10_tickdone.databinding.FragmentHomeBinding
import com.example.kelompok10_tickdone.models.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalenderFragment : Fragment() {
    private var _binding: FragmentCalenderBinding? = null
    private val binding get() = _binding!!
    private lateinit var tasksList: ArrayList<Task>
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalenderBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi Firebase
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser ?: throw IllegalStateException("User not logged in")
        firebaseRef = FirebaseDatabase.getInstance().getReference("tasks")
        tasksList = ArrayList()
        val currentDateTextView = view.findViewById<TextView>(R.id.current_date)
        val todoHeaderTextView = view.findViewById<TextView>(R.id.todo_header)
        // Daftar TextView untuk tanggal
        val dateTextViews = listOf(
            binding.date1, binding.date2, binding.date3,
            binding.date4, binding.date5, binding.date6, binding.date7
        )

        // Daftar tanggal
        val today = LocalDate.now()
        currentDateTextView.text = "Today • ${today.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }}"
        todoHeaderTextView.text = "Tasks for ${today.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }}"
        val dates = dateTextViews.mapIndexed { index, _ -> today.plusDays(index.toLong()) }

        // Atur tanggal pada TextView
        dateTextViews.forEachIndexed { index, textView ->
            textView.text = dates[index].dayOfMonth.toString()
            textView.setOnClickListener {
                // Perbarui latar belakang semua TextView
                dateTextViews.forEach { it.setBackgroundResource(R.drawable.unselected_day_background) }
                // Perbarui latar belakang TextView yang diklik
                textView.setBackgroundResource(R.drawable.selected_day_background)
                todoHeaderTextView.text = "Tasks for ${dates[index].dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }}"
                // Tampilkan tugas untuk tanggal yang dipilih
                fetchTasksForDate(dates[index])
            }
        }

        // Atur RecyclerView
        binding.rvTasks.layoutManager = LinearLayoutManager(requireContext())
        fetchTasksForDate(today) // Default menampilkan tugas hari ini
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchTasksForDate(date: LocalDate) {
        val selectedDateString = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) // Format date as string

        firebaseRef
            .orderByChild("user")
            .equalTo(user.uid) // Filter tasks by the current user's UID
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    tasksList.clear()
                    if (snapshot.exists()) {
                        for (taskSnap in snapshot.children) {
                            val task = taskSnap.getValue(Task::class.java)
                            // Ensure the task's date matches the selected date
                            if (task != null && task.date == selectedDateString) {
                                tasksList.add(task)
                            }
                        }
                    }
                    val rvAdapter = RvTasksAdapter(tasksList)
                    binding.rvTasks.adapter = rvAdapter
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
