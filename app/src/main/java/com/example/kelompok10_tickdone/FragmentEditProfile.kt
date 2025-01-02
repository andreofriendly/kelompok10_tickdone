package com.example.kelompok10_tickdone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kelompok10_tickdone.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class EditProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var firebaseref: DatabaseReference

    private lateinit var editName: EditText
    private lateinit var btnSend: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser ?: run {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return view
        }
        firebaseref = FirebaseDatabase.getInstance().getReference("user")

        // Initialize UI Elements
        editName = view.findViewById(R.id.editName)
        btnSend = view.findViewById(R.id.btnSend)

        // Load User Data
        loadUserData()

        // Handle Update Button Click
        btnSend.setOnClickListener {
            updateUserData()
        }

        return view
    }

    private fun loadUserData() {
        firebaseref.child(user.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val userDisplay = dataSnapshot.getValue(User::class.java)
                    userDisplay?.let {
                        editName.setText(it.name)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUserData() {
        val name = editName.text.toString().trim()

        if (name.isEmpty()) {
            Toast.makeText(requireContext(), "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val id = user.uid
        val userData = User(id, name)

        firebaseref.child(id).setValue(userData)
            .addOnCompleteListener {
                Toast.makeText(requireContext(), "Data user berhasil diupdate", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
