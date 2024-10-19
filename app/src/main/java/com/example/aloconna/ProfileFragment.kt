package com.example.aloconna

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.aloconna.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    lateinit var userDB : DatabaseReference
    private var userId = ""

    private var bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(layoutInflater,container,false)

        userDB = FirebaseDatabase.getInstance().reference

        requireArguments().getString("id")?.let {

            userId = it
            getUserById(it)

        }
        FirebaseAuth.getInstance().currentUser?.let {

            if (it.uid == userId){

                binding.letsChatBtn.text = EDIT

            }else{
                binding.letsChatBtn.text = CHAT
            }

        }

        binding.letsChatBtn.setOnClickListener {

            if (binding.letsChatBtn.text == EDIT){
                bundle.putString(USERID,userId)
            findNavController().navigate(R.id.action_profileFragment_to_profileEditFragment,bundle)

            }

        }







        return binding.root
    }

    companion object{


        private var EDIT = "Lets Edit"
        private var CHAT = "Lets Chat"
        private var USERID = "id"

    }

    private fun getUserById(userId: String) {

        userDB.child(DBNODES.USER).child(userId).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(User::class.java)?.let {
                    binding.apply {

                        fullName.text = it.fullName
                        bioTV.text = it.bio
                        emailTV.text = it.email
                        profileIV.load(it.profilePic)



                    }



                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })

    }


}