package com.example.kelompok10_tickdone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.kelompok10_tickdone.databinding.FragmentAddBinding
import com.example.kelompok10_tickdone.databinding.FragmentUpdateBinding
import com.example.kelompok10_tickdone.models.Status
import com.example.kelompok10_tickdone.models.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateFragment : Fragment() {



    private var _binding : FragmentUpdateBinding? = null
    private val binding get() = _binding!!
    private val args : UpdateFragmentArgs by navArgs()
    private lateinit var firebaseRef : DatabaseReference
    private lateinit var firebaseRef2 : DatabaseReference
    private lateinit var auth: FirebaseAuth;
    private lateinit var user: FirebaseUser;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View {

        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        firebaseRef = FirebaseDatabase.getInstance().getReference("tasks")
        firebaseRef2 = FirebaseDatabase.getInstance().getReference("statuses")
        firebaseRef2.child(args.id).get().addOnSuccessListener { snapshot ->
            val statusModel = snapshot.getValue(Status::class.java)
            binding.checkboxTaskStatus.isChecked = statusModel?.status == true
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to load status: ${it.message}", Toast.LENGTH_SHORT).show()
        }

        binding.apply {
            editUpdateName.setText(args.name)
            editUpdateDescription.setText(args.description)
            editUpdateDate.setText(args.date)
            editUpdateTime.setText(args.time)
            btnUpdate.setOnClickListener(){
                updateData()
            }
        }
        return binding.root
    }



    private fun updateData() {

        val name = binding.editUpdateName.text.toString()
        val description = binding.editUpdateDescription.text.toString()
        val date = binding.editUpdateDate.text.toString()
        val time = binding.editUpdateTime.text.toString()
        val task = Task(args.id, name, description, date, time, user.uid)

        firebaseRef.child("tasks").child(args.id).setValue(task).addOnCompleteListener(){
            Toast.makeText(context, "Task updated successfully", Toast.LENGTH_SHORT).show()
            updateStatus()
        }.addOnFailureListener(){
            Toast.makeText(context, "Error ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateStatus() {
        val taskId = args.id
        val userId = user.uid
        val status = binding.checkboxTaskStatus.isChecked

        val statusModel = Status(task_id = taskId, user_id = userId, status = status)

        firebaseRef2.child(taskId).setValue(statusModel)
            .addOnCompleteListener {
                Toast.makeText(context, "Status updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }


}