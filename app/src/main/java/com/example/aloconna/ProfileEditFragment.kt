package com.example.aloconna

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.aloconna.databinding.FragmentProfileEditBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileEditFragment : Fragment() {
    lateinit var binding: FragmentProfileEditBinding
    lateinit var userDB : DatabaseReference

    private var userId = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileEditBinding.inflate(layoutInflater,container,false)


        userDB = FirebaseDatabase.getInstance().reference






        requireArguments().getString("id")?.let {
            userId = it
            getUSerById(it)

        }


        binding.saveBtn.setOnClickListener {

            var userMap : MutableMap<String,Any> = mutableMapOf()

            userMap["fullName"] = binding.fullName.text.toString().trim()
            userMap["bio"] = binding.bioTV.text.toString().trim()


            userDB.child(DBNODES.USER).child(userId).updateChildren(userMap).addOnCompleteListener { task ->

                if (task.isSuccessful){
                    Toast.makeText(requireContext(), "Successfully Updated", Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(requireContext(), "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }

            }


        }






        return binding.root
    }

    private fun getUSerById(it: String) {

        userDB.child(DBNODES.USER).child(userId).addValueEventListener(
            object  : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.getValue(User ::class.java)?.let {

                        binding.apply {

                            fullName.setText( it.fullName)
                            bioTV.setText(it.bio)
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            }
        )

    }



}


