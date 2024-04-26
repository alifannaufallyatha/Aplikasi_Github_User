package com.dicoding.aplikasigithubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.aplikasigithubuser.R
import com.dicoding.aplikasigithubuser.adapter.UserListAdapter
import com.dicoding.aplikasigithubuser.adapter.UserSearchAdapter
import com.dicoding.aplikasigithubuser.data.remote.response.GithubUserResponseItem
import com.dicoding.aplikasigithubuser.data.remote.response.ItemsItem
import com.dicoding.aplikasigithubuser.databinding.ActivityMainBinding
import com.dicoding.aplikasigithubuser.utils.SettingPreferences
import com.dicoding.aplikasigithubuser.utils.dataStore
import com.dicoding.aplikasigithubuser.viewmodel.MainViewModel
import com.dicoding.aplikasigithubuser.viewmodel.SettingViewModel
import com.dicoding.aplikasigithubuser.viewmodel.SettingViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java)
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUserList.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUserList.addItemDecoration(itemDecoration)

        mainViewModel.findGithubUserResponse()
        mainViewModel.setListGithubUserData.observe(this){setListGithubData->
            setListGithubData(setListGithubData)
        }

        mainViewModel.errorMessage.observe(this) { errorMessage ->
            showError(errorMessage)
        }

        mainViewModel.isLoading.observe(this){
            showLoading(it)
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
                searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchView.hide()
                    val searchText = searchView.text.toString()
                    if (searchText.isNotEmpty()) {
                        mainViewModel.searchUserItem(searchText)
                    } else {
                        mainViewModel.findGithubUserResponse()
                    }
                    false
                }
        }


        mainViewModel.searchUser.observe(this){ user ->
            setSearchGithubData(user)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_github_user, menu)
        return super.onCreateOptionsMenu(menu)
    }

     private fun setListGithubData(githubUserResponse: List<GithubUserResponseItem>){
        val adapter = UserListAdapter{
            selectedUser ->
            startActivity(Intent(this@MainActivity, DetailActivity::class.java).putExtra("ID_USER",selectedUser.login))
        }
        adapter.submitList(githubUserResponse)
         binding.rvUserList.setHasFixedSize(true)
        binding.rvUserList.adapter = adapter
    }

    private fun setSearchGithubData(user: List<ItemsItem>){
        val adapter = UserSearchAdapter{
            selectedUser ->
            startActivity(Intent(this@MainActivity, DetailActivity::class.java).putExtra("ID_USER",selectedUser.login))
        }
        adapter.submitList(user)
        binding.rvUserList.setHasFixedSize(true)
        binding.rvUserList.adapter = adapter
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settingBtn -> {
                startActivity(Intent(this@MainActivity, SettingActivity::class.java))
            }
            R.id.favBtn -> {
                startActivity(Intent(this@MainActivity, FavoriteUsersActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

}