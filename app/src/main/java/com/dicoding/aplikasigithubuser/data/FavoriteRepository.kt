package com.dicoding.aplikasigithubuser.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dicoding.aplikasigithubuser.data.local.entity.FavoriteUserEntity
import com.dicoding.aplikasigithubuser.data.local.room.FavoriteUserDao
import com.dicoding.aplikasigithubuser.data.remote.response.DetailResponse
import com.dicoding.aplikasigithubuser.data.remote.response.GithubUserResponseItem
import com.dicoding.aplikasigithubuser.data.remote.retrofit.ApiService
import com.dicoding.aplikasigithubuser.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class FavoriteRepository private constructor(
    private val apiService: ApiService,
    private val favoritesDao: FavoriteUserDao,
    private val appExecutors: AppExecutors
){
    private val result = MediatorLiveData<Result<List<FavoriteUserEntity>>>()

    fun getListUserRepository(): LiveData<Result<List<FavoriteUserEntity>>> {
        result.value = Result.Loading
        val client = apiService.getListUserGithub()
        client.enqueue(object : Callback<List<GithubUserResponseItem>> {
            override fun onResponse(call: Call<List<GithubUserResponseItem>>, response: Response<List<GithubUserResponseItem>>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    val favoritedList = ArrayList<FavoriteUserEntity>()
                    appExecutors.diskIO.execute {
                        users?.forEach { user ->
                            val isFavorited = favoritesDao.isUserFavorite(user.login)
                            val favoritedUser = FavoriteUserEntity(
                                user.login,
                                user.avatarUrl,
                                isFavorited
                            )

                            favoritedList.add(favoritedUser)
                        }
                        favoritesDao.deleteAll()
                        favoritesDao.insertFavoritedUser(favoritedList)
                    }
                }
            }

            override fun onFailure(call: Call<List<GithubUserResponseItem>>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        val localData = favoritesDao.getFavorites()
        result.addSource(localData) { newData: List<FavoriteUserEntity> ->
            result.value = Result.Success(newData)
        }
        return result
    }

    fun getFavUserByUsername(username: String): LiveData<Result<List<FavoriteUserEntity>>> {
        result.value = Result.Loading
        val client = apiService.getDetailUser(username)
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    val favoritedList = ArrayList<FavoriteUserEntity>()
                    appExecutors.diskIO.execute {

                        val isFavorited = favoritesDao.isUserFavorite(users?.login ?: "Null")
                        val favortedUser = FavoriteUserEntity(
                            users?.login ?: "Null",
                            users?.avatarUrl ?: "",
                            isFavorited
                        )

                        favoritedList.add(favortedUser)
                        favoritesDao.deleteAll()
                        favoritesDao.insertFavoritedUser(favoritedList)
                    }
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        val localData = favoritesDao.getFavorites()
        result.addSource(localData) { newData: List<FavoriteUserEntity> ->
            result.value = Result.Success(newData)
        }
        return result
    }

    fun getFavoriteUser(username: String): LiveData<FavoriteUserEntity> {
        return favoritesDao.getUserFavorite(username)
    }

    fun getListFavoritedUser(): LiveData<List<FavoriteUserEntity>> {
        return favoritesDao.getFavoritedUser()
    }

    fun setFavoriteUser(favorites: FavoriteUserEntity, favoriteState: Boolean){
        appExecutors.diskIO.execute{
            favorites.isFavorited = favoriteState
            favoritesDao.updateFavoritedUser(favorites)
        }
    }

    companion object {
        @Volatile
        private var instance: FavoriteRepository? = null
        fun getInstance(
            apiService: ApiService,
            favoritesDao: FavoriteUserDao,
            appExecutors: AppExecutors
        ): FavoriteRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteRepository(apiService, favoritesDao, appExecutors)
            }.also { instance = it }
    }
}
