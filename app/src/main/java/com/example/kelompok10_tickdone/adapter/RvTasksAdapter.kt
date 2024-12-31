package com.example.kelompok10_tickdone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kelompok10_tickdone.HomeFragment
import com.example.kelompok10_tickdone.HomeFragmentDirections
import com.example.kelompok10_tickdone.R
import com.example.kelompok10_tickdone.databinding.RvTasksItemBinding
import com.example.kelompok10_tickdone.models.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase

class RvTasksAdapter(private val taskList : java.util.ArrayList<Task>) : RecyclerView.Adapter<RvTasksAdapter.ViewHolder>() {
    class ViewHolder(val binding: RvTasksItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RvTasksItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = taskList[position]
        holder.apply {
            binding.apply {
                tvNameItem.text = currentItem.name
                tvDescriptionItem.text = currentItem.description
                tvDateItem.text = currentItem.date
                tvTimeItem.text = currentItem.time

                // Load image using Glide
                if (currentItem.imageUri != null) {
                    Glide.with(holder.itemView.context)
                        .load(currentItem.imageUri)
                        .placeholder(R.drawable.task_background) // Gambar placeholder
                        .into(taskImage)
                } else {
                    taskImage.setImageResource(R.drawable.task_background)
                }

                rvContainer.setOnClickListener {
                    val navController = findNavController(holder.itemView)
                    val currentDestination = navController.currentDestination?.id

                    if (currentDestination == R.id.homeFragment) {
                        val action = HomeFragmentDirections.actionHomeFragmentToUpdateFragment(
                            currentItem.id.toString(),
                            currentItem.name.toString(),
                            currentItem.description.toString(),
                            currentItem.date.toString(),
                            currentItem.time.toString(),
                            currentItem.imageUri.toString() // Pass image URI
                        )
                        navController.navigate(action)
                    }
                }

                rvContainer.setOnLongClickListener {
                    MaterialAlertDialogBuilder(holder.itemView.context)
                        .setTitle("Delete item permanently")
                        .setMessage("Are you sure want to delete this task?")
                        .setPositiveButton("Yes") { _, _ ->
                            val firebaseRef = FirebaseDatabase.getInstance().getReference("tasks")
                            firebaseRef.child(currentItem.id.toString()).removeValue()
                                .addOnSuccessListener {
                                    Toast.makeText(holder.itemView.context, "Tasks removed successfully", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(holder.itemView.context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .setNegativeButton("No") { _, _ ->
                            Toast.makeText(holder.itemView.context, "Cancelled", Toast.LENGTH_SHORT).show()
                        }
                        .show()
                    true
                }
            }
        }
    }

}