package com.example.kelompok10_tickdone

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.kelompok10_tickdone.databinding.FragmentProfileBinding
import com.example.kelompok10_tickdone.models.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth;
    private lateinit var user: FirebaseUser;
    private lateinit var firebaseRef: DatabaseReference;
    private lateinit var firebaseRef2: DatabaseReference;
    private lateinit var userName: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        firebaseRef = FirebaseDatabase.getInstance().getReference("user")
        firebaseRef2 = FirebaseDatabase.getInstance().getReference("statuses")




        userName = binding.profileName
        firebaseRef.child(user.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val userDisplay = dataSnapshot.getValue(User::class.java)
                    if (userDisplay != null) {
                        userName.setText(userDisplay.name)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        firebaseRef2.orderByChild("user_id").equalTo(user.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    var trueCount = 0
                    var falseCount = 0

                    for (child in snapshot.children) {
                        val status = child.child("status").getValue(Boolean::class.java)
                        if (status == true) {
                            trueCount++
                        } else if (status == false) {
                            falseCount++
                        }
                    }
                    binding.taskLeft.setText("$falseCount Task Left")
                    binding.taskDone.setText("$trueCount Task Done")

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        // Menambahkan listener untuk mengubah nama akun
        binding.changeAccountNameLayout.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        binding.changeAccountPasswordLayout.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editPasswordFragment)
        }

        binding.logout.setOnClickListener {

            // Redirect to the login screen
            val intent = Intent(requireContext(), OnboardingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
