package com.dicoding.aplikasigithubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.aplikasigithubuser.adapter.UserListAdapter.MyViewHolder.Companion.DIFF_CALLBACK
import com.dicoding.aplikasigithubuser.data.remote.response.GithubUserResponseItem
import com.dicoding.aplikasigithubuser.databinding.UserItemBinding

class UserListAdapter(private val onItemClick: (GithubUserResponseItem) -> Unit)  : ListAdapter<GithubUserResponseItem, UserListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val listUser = getItem(position)
        holder.bind(listUser)
        holder.itemView.setOnClickListener {
            onItemClick(listUser)
        }
    }

    class MyViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listUser: GithubUserResponseItem) {
            binding.tvCardNameUser.text = listUser.login

            Glide.with(binding.root)
                .load(listUser.avatarUrl).into(binding.cardImageProfile)
        }


        companion object {
            val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GithubUserResponseItem>() {
                override fun areItemsTheSame(
                    oldItem: GithubUserResponseItem,
                    newItem: GithubUserResponseItem
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: GithubUserResponseItem,
                    newItem: GithubUserResponseItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}