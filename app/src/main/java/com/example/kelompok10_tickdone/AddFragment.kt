package com.example.kelompok10_tickdone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kelompok10_tickdone.databinding.FragmentAddBinding
import com.example.kelompok10_tickdone.databinding.FragmentHomeBinding
import com.example.kelompok10_tickdone.models.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddFragment : Fragment() {
    private var _binding : FragmentAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseRef : DatabaseReference
    private lateinit var auth: FirebaseAuth;
    private lateinit var user: FirebaseUser;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        firebaseRef = FirebaseDatabase.getInstance().getReference("tasks")
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!

    binding.btnSend.setOnClickListener(){
        saveData()
    }

        return binding.root
    }

    private fun saveData() {
        val name = binding.editName.text.toString()
        val description = binding.editDescription.text.toString()
        val date = binding.editDate.text.toString()
        val time = binding.editTime.text.toString()

        if(name.isEmpty()) binding.editName.error = "Input the task name"
        if(description.isEmpty()) binding.editName.error = "Input the task description"
        if(date.isEmpty()) binding.editName.error = "Input the date"
        if(time.isEmpty()) binding.editName.error = "Input the time"

        val taskId = firebaseRef.push().key!!
        val task = Task(taskId, name, description, date, time, user.uid)

        firebaseRef.child(taskId).setValue(task).addOnCompleteListener(){
            Toast.makeText(context, "Task saved successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener(){
            Toast.makeText(context, "Error ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }


}