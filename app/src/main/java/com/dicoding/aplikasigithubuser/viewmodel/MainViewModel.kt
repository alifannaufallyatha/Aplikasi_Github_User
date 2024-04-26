package com.dicoding.aplikasigithubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.aplikasigithubuser.data.remote.response.ErrorResponse
import com.dicoding.aplikasigithubuser.data.remote.response.GithubUserResponseItem
import com.dicoding.aplikasigithubuser.data.remote.response.ItemsItem
import com.dicoding.aplikasigithubuser.data.remote.response.SearchResponse
import com.dicoding.aplikasigithubuser.data.remote.retrofit.ApiConfig
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _setListGithubData= MutableLiveData<List<GithubUserResponseItem>>()
    val setListGithubUserData: LiveData<List<GithubUserResponseItem>> = _setListGithubData

    private val _user = MutableLiveData<SearchResponse>()
    val user: LiveData<SearchResponse> = _user

    private val _searchUser = MutableLiveData<List<ItemsItem>>()
    val searchUser: LiveData<List<ItemsItem>> = _searchUser

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "MainViewModel"
    }
    init{

        findGithubUserResponse()
    }
     fun findGithubUserResponse() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListUserGithub()
        client.enqueue(object : Callback<List<GithubUserResponseItem>> {

            override fun onResponse(
                call: Call<List<GithubUserResponseItem>>,
                response: Response<List<GithubUserResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _setListGithubData.value = responseBody
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<GithubUserResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun searchUserItem (textSearch:String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUser(textSearch)

        client.enqueue(object : Callback<SearchResponse>{
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>){
                _isLoading.value = false
                if (response.isSuccessful){
                    _user.value = response.body()
                    _searchUser.value = response.body()?.items
                }else{
                    handleErrorResponse(response)
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })

    }
    private fun handleErrorResponse(response: Response<SearchResponse>) {
        try {
            val errorBody = response.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)

            val errorMessage = "Error: ${errorResponse.message}, Code: ${errorResponse.errors[0].code}"
            _errorMessage.value = errorMessage

        } catch (e: Exception) {
            _errorMessage.value = "Error parsing error response."
            Log.e(TAG, "Error parsing error response: $e")
        }
    }

}