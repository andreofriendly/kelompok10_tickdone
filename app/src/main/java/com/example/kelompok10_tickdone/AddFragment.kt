package com.example.kelompok10_tickdone

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.kelompok10_tickdone.databinding.FragmentAddBinding
import com.example.kelompok10_tickdone.models.Status
import com.example.kelompok10_tickdone.models.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddFragment : Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var firebaseRef2: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private var imageUri: Uri? = null // Variable to hold selected image URI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        firebaseRef = FirebaseDatabase.getInstance().getReference("tasks")
        firebaseRef2 = FirebaseDatabase.getInstance().getReference("statuses")
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!

        // Set up click listeners for Date and Time pickers
        binding.editDate.setOnClickListener {
            showDatePicker()
        }

        binding.editTime.setOnClickListener {
            showTimePicker()
        }

        // Set up click listener for selecting an image
        binding.btnChoosePhoto.setOnClickListener {
            pickImage()
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
                val selectedDate =
                    String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year)
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
                val selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
                binding.editTime.text = selectedTime
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == android.app.Activity.RESULT_OK) {
            imageUri = data?.data
            binding.imagePreview.setImageURI(imageUri) // Preview the selected image
        }
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
        val imagePath = saveImageLocally(taskId) // Save image locally and get its path

        val task = Task(taskId, name, description, date, time, user.uid, imagePath)

        firebaseRef.child(taskId).setValue(task).addOnCompleteListener {
            Toast.makeText(context, "Task saved successfully", Toast.LENGTH_SHORT).show()

            val statusModel = Status(task_id = taskId, user_id = user.uid, status = false)

            firebaseRef2.child(taskId).setValue(statusModel)
                .addOnCompleteListener {
                    Toast.makeText(context, "Status updated successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error ${it.message}", Toast.LENGTH_SHORT).show()
                }

            findNavController().navigate(R.id.action_addFragment_to_homeFragment)
        }.addOnFailureListener {
            Toast.makeText(context, "Error ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageLocally(taskId: String): String? {
        return if (imageUri != null) {
            try {
                val inputStream = context?.contentResolver?.openInputStream(imageUri!!)
                val file = File(context?.filesDir, "$taskId.jpg")
                val outputStream = FileOutputStream(file)

                inputStream?.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }
                file.absolutePath // Return the file path
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Failed to save image locally", Toast.LENGTH_SHORT).show()
                null
            }
        } else {
            null // Return null if no image is selected
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1001
    }
}
