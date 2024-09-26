

package com.example.aloconna

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.aloconna.databinding.FragmentLooginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LooginFragment : Fragment() {
    lateinit var binding: FragmentLooginBinding

    lateinit var firebaseUser: FirebaseUser


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLooginBinding.inflate(inflater,container,false)

        FirebaseAuth.getInstance().currentUser?.let {

            firebaseUser = it

            findNavController().navigate(R.id.action_looginFragment_to_homeFragment)
        }

        binding.loginBTN.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val password  = binding.passEt.text.toString().trim()

            if(isEmailValid(email) && isPasswordValid(password)){
                loginUser(email, password)


            }else{
                Toast.makeText(requireContext(),"INVALID EMAIL AND PASSWORD" ,Toast.LENGTH_LONG).show()
            }
        }


        binding.creatTV.setOnClickListener{
            findNavController().navigate(R.id.action_looginFragment_to_singinFragment)
        }





        return binding.root
    }
   private fun loginUser(email: String,password: String){


        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {task->

            if (task.isSuccessful){
                val user= auth.currentUser

                Toast.makeText(requireContext(),"Login Successfully ${user?.email}",Toast.LENGTH_LONG).show()

            }else{
                Toast.makeText(requireContext(),"${task.exception?.message}",Toast.LENGTH_LONG).show()
            }
            findNavController().navigate(R.id.action_looginFragment_to_homeFragment)

        }


    }
    fun isEmailValid (email: String):Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun isPasswordValid (password: String): Boolean{
        return password.length>=6
    }


}