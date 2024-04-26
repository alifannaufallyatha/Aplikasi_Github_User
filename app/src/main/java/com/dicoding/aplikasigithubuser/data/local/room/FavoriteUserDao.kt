package com.dicoding.aplikasigithubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.aplikasigithubuser.data.local.entity.FavoriteUserEntity

@Dao
interface FavoriteUserDao {
    @Query("SELECT * FROM favorite_user")
    fun getFavorites(): LiveData<List<FavoriteUserEntity>>

    @Query("SELECT * FROM favorite_user where favorited = 1")
    fun getFavoritedUser(): LiveData<List<FavoriteUserEntity>>

    @Query("SELECT * FROM favorite_user where login = :login")
    fun searchUser(login: String): LiveData<List<FavoriteUserEntity>>

    @Query("SELECT * FROM favorite_user where login = :login")
    fun getUserFavorite(login: String): LiveData<FavoriteUserEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavoritedUser(favoritedUser: List<FavoriteUserEntity>)

    @Update
    fun updateFavoritedUser(favorited: FavoriteUserEntity)

    @Query("DELETE FROM favorite_user where favorited = 0")
    fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM favorite_user WHERE login = :login AND favorited = 1)")
    fun isUserFavorite(login: String): Boolean
}