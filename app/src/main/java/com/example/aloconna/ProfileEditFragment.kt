package com.example.aloconna

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.aloconna.databinding.FragmentProfileEditBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference


class ProfileEditFragment : Fragment() {
    lateinit var binding: FragmentProfileEditBinding
    lateinit var userDB : DatabaseReference

    private var userId = ""

    private lateinit var userProfileUri: Uri

    lateinit var userStorage: StorageReference

    private var isProfileClicked = false

    private var imageLink : String = "no link"


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



        isProfileClicked = true
        pickProfileImage()

        return binding.root
    }

    private fun pickProfileImage() {

        ImagePicker.with(this)
            .crop()                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .createIntent { intent ->

                startForProfileImageResult.launch(intent)
            }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    data?.data.let {
                        if (it != null) {
                            userProfileUri = it
                        }
                        binding.profileIV.setImageURI(it)
                    }
                }

                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
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


