package com.example.projectq.data.remote.apiservice

import com.example.projectq.data.remote.model.response.DetailUserResponse
import com.example.projectq.data.remote.model.response.GitHubResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    companion object {
        const val GET_USERS = "search/users"
        const val GET_USER_DETAIL = "users/{username}"
        const val GET_LIST_FOLLOWING = "users/{username}/following"
        const val GET_LIST_FOLLOWERS = "users/{username}/followers"
    }

    @GET(GET_USERS)
    suspend fun getListUser(
        @Query("q") username: String
    ): Response<GitHubResponse>

    @GET(GET_USER_DETAIL)
    suspend fun getUserDetail(
        @Path("username") username: String
    ): Response<DetailUserResponse>

    @GET(GET_LIST_FOLLOWING)
    suspend fun getListUserFollowing(
        @Path("username") username: String
    ): Response<List<DetailUserResponse>>

    @GET(GET_LIST_FOLLOWERS)
    suspend fun getListUserFollowers(
        @Path("username") username: String
    ): Response<List<DetailUserResponse>>
}