package com.dicoding.aplikasigithubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.aplikasigithubuser.data.remote.response.ErrorGetFollowers
import com.dicoding.aplikasigithubuser.data.remote.response.FollowersResponse
import com.dicoding.aplikasigithubuser.data.remote.response.FollowersResponseItem
import com.dicoding.aplikasigithubuser.data.remote.retrofit.ApiConfig
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel: ViewModel() {

    private val _followers = MutableLiveData<List<FollowersResponse>>()
    val followers : LiveData<List<FollowersResponse>> = _followers

    private val _listFollowers= MutableLiveData<List<FollowersResponseItem>>()
    val listFollowers : LiveData<List<FollowersResponseItem>> = _listFollowers

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "MainActivity"
    }
    fun getFollowerUsername (username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowerUsername(username)

        client.enqueue(object: Callback<List<FollowersResponseItem>> {
            override fun onResponse(call: Call<List<FollowersResponseItem>>, response: Response<List<FollowersResponseItem>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollowers.value = response.body()
                    Log.e(TAG, "ON SUCCUES User Following: ${response.body()}")
                } else {
                    handleErrorResponse(response)
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowersResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

        private fun handleErrorResponse(response: Response<List<FollowersResponseItem>>) {
            try {
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorGetFollowers::class.java)

                val errorMessage = "Error: ${errorResponse.message}"
                _errorMessage.value = errorMessage

            } catch (e: Exception) {
                _errorMessage.value = "Error parsing error response."
                Log.e(TAG, "Error parsing error response: $e")
            }
        }
    }
