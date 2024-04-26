package com.dicoding.aplikasigithubuser.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.aplikasigithubuser.data.local.entity.FavoriteUserEntity
import com.dicoding.aplikasigithubuser.databinding.UserItemBinding

class FavoriteAdapter (private val onItemClick: (FavoriteUserEntity) -> Unit) : ListAdapter<FavoriteUserEntity, FavoriteAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding= UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        holder.itemView.setOnClickListener{
            onItemClick(user)
        }
    }

    class MyViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(favorites: FavoriteUserEntity) {
            binding.tvCardNameUser.text = favorites.login
            Glide.with(binding.root)
                .load(favorites.avatarUrl)
                .into(binding.cardImageProfile)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteUserEntity>() {
            override fun areItemsTheSame(oldItem: FavoriteUserEntity, newItem: FavoriteUserEntity): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: FavoriteUserEntity, newItem: FavoriteUserEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}