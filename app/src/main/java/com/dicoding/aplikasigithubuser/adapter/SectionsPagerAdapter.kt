package com.dicoding.aplikasigithubuser.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.aplikasigithubuser.fragment.FollowersFragment
import com.dicoding.aplikasigithubuser.fragment.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity):FragmentStateAdapter(activity) {

    private var username : String = ""
    fun setUsernameGit (user: String){
        this.username = user
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FollowersFragment().apply {
                arguments = Bundle().apply {
                    putString("username", username)
                }
            }

            1 -> FollowingFragment().apply {
                arguments = Bundle().apply {
                    putString("username", username)
                }
            }
            else -> throw IllegalArgumentException("Invalid positions : $position")
        }
    }

    override fun getItemCount(): Int {
        return 2
    }

}