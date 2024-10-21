package com.example.aloconna

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.example.aloconna.databinding.ProfileItemBinding


class UserAdapter(var itemClick: ItemClick) : ListAdapter<User, UserViewHolder>(comparator) {
lateinit var context : Context

    interface ItemClick {

        fun onItemClick(user: User)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

context = parent.context


        return UserViewHolder(
            ProfileItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {


        getItem(position).let {

            holder.binding.apply {

                pfName.text = it.fullName
                gmailName.text = it.email
                bioTV.text = it.bio
                Glide.with(context).load(it.profilePic).placeholder(R.drawable.playholder)
                    .into(profileIV)

            }

            holder.itemView.setOnClickListener { _ ->

                itemClick.onItemClick(it)

            }


        }


    }

    companion object {

        var comparator = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }


        }

    }


}


class UserViewHolder(var binding: ProfileItemBinding) : RecyclerView.ViewHolder(binding.root) {

}