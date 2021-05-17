package com.example.euniversity.network.service

import com.example.euniversity.network.response.*

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.time.LocalDateTime

interface CommunityService {
    @GET("community/findAllProblemAnswer")
    fun findAllProblemAnswer(
        @Query("time") time:Int
    ): Call<Response<ProblemAnswer>>
    @POST("community/askProblem")
    fun askProblem(
        @Query("content") content:String,
        @Query("time") time:String,
        @Query("userPhone") userPhone:String
    ):Call<Response<Problem>>

    @GET("community/myProblem")
    fun myProblem(
        @Query("userPhone") userPhone:String
    ):Call<Response<ProblemAnswer>>

    @GET("community/myAnswer")
    fun myAnswer(
        @Query("userPhone") userPhone:String
    ):Call<Response<ProblemAnswer>>

    @GET("community/commonProblem")
    fun commonProblem(): Call<Response<ProblemAnswer>>

    @GET("community/qualitySortProblem")
    fun qualitySortProblem(
        @Query("time") time:Int
    ): Call<Response<ProblemAnswer>>

    @GET("community/comprehensiveSortProblem")
    fun comprehensiveSortProblem(
        @Query("time") time:Int
    ): Call<Response<ProblemAnswer>>

    @GET("community/searchProblem")
    fun searchProblem(
        @Query("text") text:String,
        @Query("time") time:Int
    ): Call<Response<ProblemAnswer>>

    @GET("community/isliked")
    fun isliked(
        @Query("userPhone") userPhone: String,
        @Query("answerId") answerId: Int
    ): Call<Response<Like>>

    @GET("community/changeToLiked")
    fun changeToLiked(
        @Query("userPhone") userPhone: String,
        @Query("answerId") answerId: Int
    ): Call<Response<Like>>

    @GET("community/changeToUnliked")
    fun changeToUnliked(
        @Query("userPhone") userPhone: String,
        @Query("answerId") answerId: Int
    ): Call<Response<Like>>

    @GET("community/updateAnswerLikes")
    fun updateAnswerLikes(
        @Query("answerId") answerId: Int,
        @Query("likes") likes:Int
    ): Call<Response<Like>>

    @GET("community/findProblemById")
    fun findProblemById(
        @Query("problemId") problemId:Int
    ):Call<Response<ProblemAnswer>>

    @POST("community/answerProblem")
    fun answerProblem(
        @Query("content") content:String,
        @Query("time") time:String,
        @Query("userPhone") userPhone:String,
        @Query("problemId") problemId: Int
    ):Call<Response<Answer>>

    @POST("community/updateAnswer")
    fun updateAnswer(
        @Query("answerId") answerId:Int,
        @Query("content") content:String,
        @Query("time") time:String
    ):Call<Response<Answer>>
}