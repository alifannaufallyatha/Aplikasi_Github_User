package com.dicoding.aplikasigithubuser.di

import android.content.Context
import com.dicoding.aplikasigithubuser.data.FavoriteRepository
import com.dicoding.aplikasigithubuser.data.local.room.FavoriteDatabase
import com.dicoding.aplikasigithubuser.data.remote.retrofit.ApiConfig
import com.dicoding.aplikasigithubuser.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): FavoriteRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteDatabase.getInstance(context)
        val dao = database.favoritesDao()
        val appExecutors = AppExecutors()
        return FavoriteRepository.getInstance(apiService, dao, appExecutors)
    }
}