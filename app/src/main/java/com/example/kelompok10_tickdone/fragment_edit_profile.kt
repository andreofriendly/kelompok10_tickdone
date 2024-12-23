package com.example.kelompok10_tickdone.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kelompok10_tickdone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.kelompok10_tickdone.models.User
import com.google.firebase.database.DatabaseError

class EditProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth;
    private lateinit var user: FirebaseUser;
    private lateinit var firebaseref: DatabaseReference;

    private lateinit var editName: EditText
    private lateinit var btnSend: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        firebaseref = FirebaseDatabase.getInstance().getReference("user")

        editName = view.findViewById(R.id.editName)
        btnSend = view.findViewById(R.id.btnSend)

        firebaseref.child(user.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val userDisplay = dataSnapshot.getValue(User::class.java)
                    if (userDisplay != null) {
                        editName.setText(userDisplay.name)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        btnSend.setOnClickListener {
            val name = editName.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = user.uid
            val userData = User(id, name)
            firebaseref.child(id).setValue(userData).addOnCompleteListener(){
                Toast.makeText(requireContext(), "Data mahasiswa berhasil diupdate", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener(){
                Toast.makeText(requireContext(), "Error ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}