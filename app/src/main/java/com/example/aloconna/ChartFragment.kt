package com.example.aloconna

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.aloconna.databinding.FragmentChartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID


class ChartFragment : Fragment() {

    lateinit var binding: FragmentChartBinding

    lateinit var chatDB: DatabaseReference
    lateinit var userIdSelf: String
    lateinit var userIdRemote: String


    val chatList = mutableListOf<TextMessage>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentChartBinding.inflate(layoutInflater,container,false)

        chatDB= FirebaseDatabase.getInstance().reference

        val Layoutmanager = LinearLayoutManager(requireContext())

        Layoutmanager.stackFromEnd = true

        binding.messageRCV.layoutManager = Layoutmanager


        requireArguments().getString(USERID)?.let {
            userIdRemote = it
        }
        binding.backBtn.setOnClickListener{
            findNavController().navigate(R.id.action_chartFragment_to_homeFragment)
        }




        FirebaseAuth.getInstance().currentUser?.let {

            userIdSelf = it.uid
        }


        chatDB.child(DBNODES.USER).child(userIdRemote).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(User::class.java)?.let {

                    binding.apply {

                        Glide.with(requireContext()).load(it.profilePic)
                            .placeholder(R.drawable.playholder).into(profileImage)

                        profileName.text = it.fullName
                        gmailName.text = it.email
                     }

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })

        messageToShow()



        return binding.root
    }

    private fun messageToShow() {
        chatDB.child(DBNODES.CHAT).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
           chatList.clear()

                snapshot.children.forEach {its->

                    its.getValue(TextMessage::class.java)?.let {

                        if (it.sanderId == userIdSelf && it.receiver == userIdRemote ||
                            it.sanderId == userIdRemote && it .receiver == userIdSelf){

                            chatList.add(it)


                        }

                    }


                }
                var adapter =  ChatAdapter(userIdSelf,chatList)


                binding.messageRCV.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
    }

    companion object{
        private var USERID = "id"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sendBtn.setOnClickListener{

            val textMessage  = TextMessage(text = binding.messageET.text.toString(),
                msgId = "",
                sanderId = userIdSelf,
                receiver = userIdRemote)
            sendMessage(textMessage)
        }
    }

    private fun sendMessage(textMessage: TextMessage) {

        val msgID = chatDB.push().key?:UUID.randomUUID().toString()

        textMessage.msgId = msgID


        chatDB.child(DBNODES.CHAT).child(msgID).setValue(textMessage).addOnCompleteListener {

            if(it.isSuccessful){
                Toast.makeText(requireContext(), "Message Sent Successfully", Toast.LENGTH_SHORT)
                    .show()
                binding.messageET.setText("")


            }else{

                Toast.makeText(requireContext(), "${it.exception?.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }


}