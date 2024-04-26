package com.dicoding.aplikasigithubuser.data.remote.retrofit

import com.dicoding.aplikasigithubuser.data.remote.response.DetailResponse
import com.dicoding.aplikasigithubuser.data.remote.response.FollowersResponseItem
import com.dicoding.aplikasigithubuser.data.remote.response.FollowingResponseItem
import com.dicoding.aplikasigithubuser.data.remote.response.GithubUserResponseItem
import com.dicoding.aplikasigithubuser.data.remote.response.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("Authorization: token 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")
    @GET("/users")
    fun getListUserGithub(): Call<List<GithubUserResponseItem>>

    @Headers("Authorization: token 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")
    @GET("/search/users")
    fun searchUser(
        @Query("q") username: String
    ): Call<SearchResponse>

    @Headers("Authorization: token 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")
    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailResponse>

    @Headers("Authorization: token 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")
    @GET("/users/{username}/followers")
    fun getFollowerUsername(
        @Path("username") username: String
    ): Call<List<FollowersResponseItem>>

    @Headers("Authorization: token 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")
    @GET("/users/{username}/following")
    fun getFollowingUsername(
        @Path("username") username: String
    ): Call<List<FollowingResponseItem>>

}