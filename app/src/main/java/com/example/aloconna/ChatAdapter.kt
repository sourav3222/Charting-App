package com.example.aloconna

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(var  userIdSelf: String): ListAdapter<TextMessage, chatViewHolder>(comparator) {


    val chatList = mutableListOf<TextMessage>()

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
        getItem(position).apply {

            chatList.add(this)
            holder.messageTV.text = this.text
        }

    }

    override fun getItemViewType(position: Int): Int {
        if (chatList[position].sanderId == userIdSelf){

            return Right
        }else{
            return Left
        }
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