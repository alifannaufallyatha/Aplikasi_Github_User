package com.dicoding.aplikasigithubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.aplikasigithubuser.data.remote.response.ErrorGetFollowing
import com.dicoding.aplikasigithubuser.data.remote.response.FollowingResponse
import com.dicoding.aplikasigithubuser.data.remote.response.FollowingResponseItem
import com.dicoding.aplikasigithubuser.data.remote.retrofit.ApiConfig
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {

    private val _following = MutableLiveData<List<FollowingResponse>>()
    val following : LiveData<List<FollowingResponse>> = _following

    private val _listFollowing = MutableLiveData<List<FollowingResponseItem>>()
    val listFollowing : LiveData<List<FollowingResponseItem>> = _listFollowing

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "MainActivity"
    }

    fun getFollowingUsername(username :String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowingUsername(username)

        client.enqueue(object: Callback<List<FollowingResponseItem>>{
            override fun onResponse(call: Call<List<FollowingResponseItem>>, response: Response<List<FollowingResponseItem>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listFollowing.value = response.body()
                    Log.e(TAG, "ON SUCCUES: ${response.body()}")
                } else {
                    handleErrorResponse(response)
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowingResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
    private fun handleErrorResponse(response: Response<List<FollowingResponseItem>>) {
        try {
            val errorBody = response.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorGetFollowing::class.java)

            val errorMessage = "Error: ${errorResponse.message}"
            _errorMessage.value = errorMessage

        } catch (e: Exception) {
            _errorMessage.value = "Error parsing error response."
            Log.e(TAG, "Error parsing error response: $e")
        }
    }


}