package com.example.aloconna

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.aloconna.databinding.FragmentSinginBinding
import com.google.firebase.auth.FirebaseAuth


class singinFragment : Fragment() {
    lateinit var binding: FragmentSinginBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding   = FragmentSinginBinding.inflate(inflater,container,false)

        binding.singInBTN.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val password  = binding.passEt.text.toString().trim()
            val user = binding.userEt.text.toString().trim()


            if (isEmailValid(email) && isPasswordValid(password)){

                singInuser(email,password,user)
            }else{
                Toast.makeText(requireContext(),"INVALID EMAIL AND PASSWORD",Toast.LENGTH_LONG).show()
            }
        }






        return binding.root
    }

    private fun singInuser(email: String, password: String, user: String) {

        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task->
            if (task.isSuccessful){
                Toast.makeText(requireContext(),"Creat Account Successfully",Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_singinFragment_to_looginFragment)
            }else{
                Toast.makeText(requireContext(),"${task.exception?.message}",Toast.LENGTH_LONG).show()
            }




        }
    }

    fun isEmailValid (email: String):Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun isPasswordValid (password: String): Boolean{
        return password.length>=6
    }

}