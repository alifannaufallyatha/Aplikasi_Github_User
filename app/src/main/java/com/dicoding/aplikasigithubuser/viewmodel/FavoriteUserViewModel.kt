package com.dicoding.aplikasigithubuser.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.aplikasigithubuser.data.FavoriteRepository
import com.dicoding.aplikasigithubuser.data.local.entity.FavoriteUserEntity

class FavoriteUserViewModel (private val favoriteRepository: FavoriteRepository) : ViewModel() {

    fun getUserFavorite() = favoriteRepository.getListUserRepository()

    fun getListFavUser() = favoriteRepository.getListFavoritedUser()

    fun getUserByUsernameFromLocal(username: String) = favoriteRepository.getFavUserByUsername(username)

    fun getUserIsFavorite(username: String) = favoriteRepository.getFavoriteUser(username)

    fun saveFavorite(favorites: FavoriteUserEntity) {
        favoriteRepository.setFavoriteUser(favorites, true)
    }

    fun deleteFavorite(favorites: FavoriteUserEntity) {
        favoriteRepository.setFavoriteUser(favorites, false)
    }
}