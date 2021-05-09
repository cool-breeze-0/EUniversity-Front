package com.example.euniversity.network.service

import com.example.euniversity.network.response.Problem
import com.example.euniversity.network.response.ProblemAnswer
import com.example.euniversity.network.response.Response

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.time.LocalDateTime

interface CommunityService {
    @GET("community/findAllProblemAnswer")
    fun findAllProblemAnswer(): Call<Response<ProblemAnswer>>
    @POST("community/askProblem")
    fun askProblem(
        @Query("content") content:String,
        @Query("time") time:String,
        @Query("userPhone") userPhone:String
    ):Call<Response<Problem>>
}