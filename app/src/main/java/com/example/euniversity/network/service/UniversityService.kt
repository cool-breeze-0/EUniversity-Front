package com.example.euniversity.network.service


import com.example.euniversity.network.response.Response
import com.example.euniversity.network.response.University
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UniversityService {
    @GET("university/findUniversity")
    fun findUniversity(
        @Query("time") time:Int
    ): Call<Response<University>>

    @GET("university/searchUniversity")
    fun searchUniversity(
        @Query("text") text:String,
        @Query("time") time:Int
    ):Call<Response<University>>

    @POST("university/filterUniversity")
    fun filterUniversity(
        @Query("universityTypeString") universityTypeString:String,
        @Query("universityLevelString") universityLevelString:String,
        @Query("educationLevelString") educationLevelString:String,
        @Query("universityNatureString") universityNatureString:String,
        @Query("provinceString") provinceString:String,
        @Query("time") time:Int
    ):Call<Response<University>>

    @GET("university/findUniversityById")
    fun findUniversityById(
        @Query("universityId") universityId:Int
    ):Call<Response<University>>

    @GET("university/findUniversityIntroductionById")
    fun findUniversityIntroductionById(
        @Query("universityId") universityId:Int
    ):Call<Response<String>>

    @GET("university/findUniversityScore")
    fun findUniversityScore(
        @Query("universityId") universityId:Int,
        @Query("province") province:String,
        @Query("division") division:String
    ):Call<Response<String>>

    @GET("university/findUniversityIdByName")
    fun findUniversityIdByName(
        @Query("universityName") universityName:String
    ):Call<Response<Int>>
}