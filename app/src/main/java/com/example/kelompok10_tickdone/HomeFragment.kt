package com.example.kelompok10_tickdone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelompok10_tickdone.adapter.RvTasksAdapter
import com.example.kelompok10_tickdone.databinding.FragmentHomeBinding
import com.example.kelompok10_tickdone.models.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var tasksList: ArrayList<Task>
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var auth: FirebaseAuth;
    private lateinit var user: FirebaseUser;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!

        binding.btnAdd.setOnClickListener(){
            findNavController().navigate(R.id.action_homeFragment_to_addFragment)
        }
        firebaseRef = FirebaseDatabase.getInstance().getReference("tasks")
        tasksList = arrayListOf()

        fetchData()

        binding.rvTasks.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
        }


        return binding.root
    }

    private fun fetchData() {
        firebaseRef.orderByChild("user").equalTo(user.uid).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                tasksList.clear()
                if(snapshot.exists()){
                    for(TaskSnap in snapshot.children){
                        val tasks = TaskSnap.getValue(Task::class.java)
                        tasksList.add(tasks!!)
                    }
                }
                val rvAdapter= RvTasksAdapter(tasksList)
                binding.rvTasks.adapter = rvAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
            }

        })
    }


}