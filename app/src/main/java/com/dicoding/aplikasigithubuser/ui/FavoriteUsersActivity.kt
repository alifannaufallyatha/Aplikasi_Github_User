package com.dicoding.aplikasigithubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.aplikasigithubuser.adapter.FavoriteAdapter
import com.dicoding.aplikasigithubuser.databinding.ActivityFavoriteUsersBinding
import com.dicoding.aplikasigithubuser.viewmodel.FavoriteUserViewModel
import com.dicoding.aplikasigithubuser.viewmodel.ViewModelFactory

class FavoriteUsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val favViewModel by viewModels<FavoriteUserViewModel>() {
            ViewModelFactory.getInstance(application)
        }

        val favAdpater = FavoriteAdapter {fav ->
            startActivity(Intent(this@FavoriteUsersActivity, DetailActivity::class.java).putExtra("ID_USER", fav.login))
        }

        favViewModel.getListFavUser().observe(this@FavoriteUsersActivity) { favUser ->
            binding?.progressBar4?.visibility = View.GONE
            favAdpater.submitList(favUser)
        }

        binding?.rvFavorite?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager(context).orientation))
            adapter = favAdpater
        }
    }
}