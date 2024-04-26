package com.dicoding.aplikasigithubuser.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_user")
class FavoriteUserEntity (

    @field:ColumnInfo(name = "login")
    @field:PrimaryKey
    val login: String = "",

    @field:ColumnInfo(name = "avatar_url")
    val avatarUrl: String? = null,

    @field:ColumnInfo(name = "favorited")
    var isFavorited: Boolean
)
