package com.example.kelompok10_tickdone

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.kelompok10_tickdone.databinding.FragmentAddBinding
import com.example.kelompok10_tickdone.models.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddFragment : Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        firebaseRef = FirebaseDatabase.getInstance().getReference("tasks")
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!

        // Set up click listeners for Date and Time pickers
        binding.editDate.setOnClickListener {
            showDatePicker()
        }

        binding.editTime.setOnClickListener {
            showTimePicker()
        }

        binding.btnSend.setOnClickListener {
            saveData()
        }

        return binding.root
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                // Update TextView with the selected date
                val selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year)
                binding.editDate.text = selectedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                // Update TextView with the selected time
                val selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
                binding.editTime.text = selectedTime
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true // Use 24-hour format
        )
        timePickerDialog.show()
    }

    private fun saveData() {
        val name = binding.editName.text.toString()
        val description = binding.editDescription.text.toString()
        val date = binding.editDate.text.toString()
        val time = binding.editTime.text.toString()

        if (name.isEmpty()) {
            binding.editName.error = "Input the task name"
            return
        }
        if (description.isEmpty()) {
            binding.editDescription.error = "Input the task description"
            return
        }
        if (date.isEmpty()) {
            Toast.makeText(context, "Input the date", Toast.LENGTH_SHORT).show()
            return
        }
        if (time.isEmpty()) {
            Toast.makeText(context, "Input the time", Toast.LENGTH_SHORT).show()
            return
        }

        val taskId = firebaseRef.push().key!!
        val task = Task(taskId, name, description, date, time, user.uid)

        firebaseRef.child(taskId).setValue(task).addOnCompleteListener {
            Toast.makeText(context, "Task saved successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_homeFragment)
        }.addOnFailureListener {
            Toast.makeText(context, "Error ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}