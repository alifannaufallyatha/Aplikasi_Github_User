package com.dicoding.aplikasigithubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.aplikasigithubuser.R
import com.dicoding.aplikasigithubuser.adapter.SectionsPagerAdapter
import com.dicoding.aplikasigithubuser.data.Result
import com.dicoding.aplikasigithubuser.data.remote.response.DetailResponse
import com.dicoding.aplikasigithubuser.databinding.DetailUserBinding
import com.dicoding.aplikasigithubuser.viewmodel.DetailViewModel
import com.dicoding.aplikasigithubuser.viewmodel.FavoriteUserViewModel
import com.dicoding.aplikasigithubuser.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity: AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: DetailUserBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userIdGithub = intent.getStringExtra("ID_USER").toString()

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.setUsernameGit(userIdGithub)

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        val favViewModel by viewModels<FavoriteUserViewModel>() {
            ViewModelFactory.getInstance(application)
        }


        val getDetailViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                DetailViewModel::class.java
            )

        getDetailViewModel.getDetailUser(userIdGithub)
        getDetailViewModel.detailUser.observe(this) { user ->
            setDataUser(user)

        }
        getDetailViewModel.errorUser.observe(this) { errorMassage ->
            showError(errorMassage)

        }
        favViewModel.getUserByUsernameFromLocal(userIdGithub).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Success -> {

                    }

                    is Result.Error -> {
                        showLoading(false)
                        showError(result.error)
                    }
                }
            }
        }


        getDetailViewModel.detailUser.observe(this) { user ->
            showLoading(false)
            setDataUser(user)
            getDetailViewModel.getDetailUser(userIdGithub)
        }

        favViewModel.getUserIsFavorite(userIdGithub).observe(this) { favUser ->
            if (favUser != null) {
                if (favUser.isFavorited) {
                    binding.fabFavorite.setImageResource(R.drawable.ic_favorite_24)
                    binding.fabFavorite.setOnClickListener {
                        binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border_24)
                        favViewModel.deleteFavorite(favUser)
                    }
                } else {
                    binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border_24)
                    binding.fabFavorite.setOnClickListener {
                        binding.fabFavorite.setImageResource(R.drawable.ic_favorite_24)
                        favViewModel.saveFavorite(favUser)
                    }
                }
            }
        }


        binding.backBtn.setOnClickListener(this)
        binding.fabFavorite.setOnClickListener(this)

        supportActionBar?.elevation = 0f

    }

    private fun setDataUser(user: DetailResponse) {
        binding.usernameDetail.text = user.login
        binding.detailName.text = user.name
        binding.followersTitle.text = "${user.followers.toString()} Followers"
        binding.repoCount.text = "${user.publicRepos.toString()} Repo"
        binding.followingTitle.text = "${user.following.toString()} Following"
        binding.gistCount.text = "${user.publicGists.toString()} Gist"
        Glide.with(this).load(user.avatarUrl).into(binding.ivItem)
    }

    private fun showError(message: String) {
        Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar5.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backBtn -> {
                startActivity(Intent(this@DetailActivity, MainActivity::class.java))
            }
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}
