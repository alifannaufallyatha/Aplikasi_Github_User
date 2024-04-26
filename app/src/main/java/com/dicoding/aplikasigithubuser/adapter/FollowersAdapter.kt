package com.dicoding.aplikasigithubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.aplikasigithubuser.data.remote.response.FollowersResponseItem
import com.dicoding.aplikasigithubuser.databinding.FollowersItemsBinding

class FollowersAdapter: ListAdapter<FollowersResponseItem, FollowersAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        val binding = FollowersItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val followersUserItem = getItem(position)
        holder.bind(followersUserItem)
    }

    class MyViewHolder(val binding: FollowersItemsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(followersUserItem: FollowersResponseItem) {
            binding.usernameFollowersLabel.text = followersUserItem.login
            Glide.with(binding.root).load(followersUserItem.avatarUrl)
                .into(binding.circleAvatarFollowers)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FollowersResponseItem>() {
            override fun areItemsTheSame(oldItem: FollowersResponseItem, newItem: FollowersResponseItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FollowersResponseItem, newItem: FollowersResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }


}