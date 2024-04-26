package com.dicoding.aplikasigithubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.aplikasigithubuser.data.remote.response.DetailResponse
import com.dicoding.aplikasigithubuser.data.remote.response.ErrorGetUser
import com.dicoding.aplikasigithubuser.data.remote.retrofit.ApiConfig
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel(){

    private val _detailUser = MutableLiveData<DetailResponse>()
    val detailUser : LiveData<DetailResponse> =_detailUser

    private val _errorUser = MutableLiveData<String>()
    val errorUser : LiveData<String> = _errorUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "MainViewModel"
    }

    fun getDetailUser (username:String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    handleErrorUser(response)
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
    private fun handleErrorUser(response: Response<DetailResponse>) {
        try {
            val errorBody = response.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorGetUser::class.java)

            val errorMessage = "Error: ${errorResponse.message}}"
            _errorUser.value = errorMessage

        } catch (e: Exception) {
            _errorUser.value = "Error parsing error response."
            Log.e(TAG, "Error parsing error response: $e")
        }
    }
}