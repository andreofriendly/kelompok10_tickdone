package com.example.kelompok10_tickdone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.kelompok10_tickdone.HomeFragment
import com.example.kelompok10_tickdone.HomeFragmentDirections
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
                rvContainer.setOnClickListener(){
                    val action = HomeFragmentDirections.actionHomeFragmentToUpdateFragment(
                        currentItem.id.toString(),
                        currentItem.name.toString(),
                        currentItem.description.toString(),
                        currentItem.date.toString(),
                        currentItem.time.toString()
                    )
                    findNavController(holder.itemView).navigate(action)
                }

                rvContainer.setOnLongClickListener(){
                    MaterialAlertDialogBuilder(holder.itemView.context).setTitle("Delete item permanently")
                        .setMessage("Are you sure want to delete this task?")
                        .setPositiveButton("Yes"){_,_ ->
                            val firebaseRef = FirebaseDatabase.getInstance().getReference("tasks")
                            firebaseRef.child(currentItem.id.toString()).removeValue()
                                .addOnSuccessListener {
                                    Toast.makeText(holder.itemView.context, "Tasks removed successfully", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener(){
                                    Toast.makeText(holder.itemView.context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        }.setNegativeButton("No"){_,_ ->
                            Toast.makeText(holder.itemView.context, "Cancelled", Toast.LENGTH_SHORT).show()
                        }
                        .show()
                    return@setOnLongClickListener true
                }
            }
        }
    }
}