package com.example.euniversity.network.service

import com.example.euniversity.network.response.Response
import com.example.euniversity.network.response.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    @POST("user/register")
    fun register(
        @Query("phone") phone:String,
        @Query("password") password:String
    ): Call<Response<User>>

    @POST("user/login")
    fun login(
        @Query("phone") phone:String,
        @Query("password") password: String
    ):Call<Response<User>>

    @POST("user/retrievePassword")
    fun retrievePassword(
        @Query("phone") phone:String,
        @Query("password") password: String
    ):Call<Response<User>>

    @POST("user/changePassword")
    fun changePassword(
        @Query("phone") phone:String,
        @Query("password") password: String,
        @Query("newPassword") newPassword:String
    ):Call<Response<User>>

    @POST("user/updateProfileInformation")
    fun updateProfileInformation(
        @Query("phone") phone:String,
        @Query("nikeName") nikeName:String,
        @Query("gender") gender:Int,
        @Query("province") province:String,
        @Query("city") city:String,
        @Query("identity") identity:String
    ):Call<Response<User>>

    @GET("user/findUserByPhone")
    fun findUserByPhone(
        @Query("phone") phone:String
    ):Call<Response<User>>
}