package com.example.aloconna

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private var  userIdSelf: String,private val chatList: MutableList<TextMessage> ):

RecyclerView.Adapter<chatViewHolder> ()  {




    var Right = 1
    var Left = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): chatViewHolder {
        if (viewType == Right) {

            var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.send_massage_item, parent, false)
            return chatViewHolder(view)
        }else{

            var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.recived_massage_item, parent, false)
            return chatViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: chatViewHolder, position: Int) {
       val message = chatList[position]
            holder.messageTV.text = message.text


    }

    override fun getItemViewType(position: Int): Int {
        return if (chatList[position].sanderId == userIdSelf){

            Right
        }else{
            Left
        }
    }

    override fun getItemCount(): Int {

        return chatList.size

    }

    companion object{
        var comparator = object : DiffUtil.ItemCallback<TextMessage>(){
            override fun areItemsTheSame(oldItem: TextMessage, newItem: TextMessage): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: TextMessage, newItem: TextMessage): Boolean {
                  return oldItem.msgId == newItem.msgId
            }


        }
    }
    }


class chatViewHolder(ItemView: android.view.View) : RecyclerView.ViewHolder(ItemView) {

    var messageTV: TextView = itemView.findViewById(R.id.chatTv)

}