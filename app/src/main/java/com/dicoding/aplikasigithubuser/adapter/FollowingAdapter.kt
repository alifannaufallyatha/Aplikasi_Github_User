package com.dicoding.aplikasigithubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.aplikasigithubuser.data.remote.response.FollowingResponseItem
import com.dicoding.aplikasigithubuser.databinding.FollowingItemsBinding

class FollowingAdapter : ListAdapter<FollowingResponseItem, FollowingAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = FollowingItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val followersUserItem = getItem(position)
        holder.bind(followersUserItem)
    }

    class MyViewHolder(val binding: FollowingItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(followingUserItem: FollowingResponseItem) {
            binding.usernameFollowingLabel.text = followingUserItem.login
            Glide.with(binding.root).load(followingUserItem.avatarUrl).into(binding.circleAvatarFollowing)
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FollowingResponseItem>() {
            override fun areItemsTheSame(oldItem: FollowingResponseItem, newItem: FollowingResponseItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FollowingResponseItem, newItem: FollowingResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}