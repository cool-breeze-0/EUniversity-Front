package com.example.euniversity.network

import com.example.euniversity.network.response.Problem
import com.example.euniversity.network.service.CommunityService
import com.example.euniversity.network.service.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query
import java.lang.RuntimeException
import java.time.LocalDateTime
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object EUniversityNetwork {
    //用户模块网络请求
    private val userService=ServiceCreator.create<UserService>()

    suspend fun register(phone:String,password:String)=
        userService.register(phone,password).await()

    suspend fun login(phone: String,password: String)=
        userService.login(phone,password).await()

    suspend fun retrievePassword(phone: String,password: String)=
        userService.retrievePassword(phone,password).await()

    suspend fun changePassword(phone: String,password: String,newPassword:String)=
        userService.changePassword(phone, password,newPassword).await()

    suspend fun updateProfileInformation(phone: String,nikeName:String,gender:Int,province:String,city:String,identity:String)=
        userService.updateProfileInformation(phone, nikeName, gender, province, city, identity).await()

    suspend fun findUserByPhone(phone: String)=
        userService.findUserByPhone(phone).await()

    //问答社区模块网络请求
    private val communityService=ServiceCreator.create<CommunityService>()

    suspend fun findAllProblemAnswer()=
        communityService.findAllProblemAnswer().await()

    suspend fun askProblem(content:String,time: String,userPhone:String)=
        communityService.askProblem(content, time, userPhone).await()

    suspend fun myProblem(userPhone:String)=
        communityService.myProblem(userPhone).await()

    suspend fun myAnswer(userPhone: String)=
        communityService.myAnswer(userPhone).await()

    suspend fun commonProblem()=
        communityService.commonProblem().await()

    suspend fun qualitySortProblem()=
        communityService.qualitySortProblem().await()

    suspend fun comprehensiveSortProblem()=
        communityService.comprehensiveSortProblem().await()

    suspend fun searchProblem(text:String)=
        communityService.searchProblem(text).await()

    suspend fun isliked(userPhone: String,answerId:Int)=
        communityService.isliked(userPhone, answerId).await()

    suspend fun changeToLiked(userPhone: String,answerId:Int)=
        communityService.changeToLiked(userPhone, answerId).await()

    suspend fun changeToUnliked(userPhone: String,answerId:Int)=
        communityService.changeToUnliked(userPhone, answerId).await()

    suspend fun updateAnswerLikes(answerId: Int,likes:Int)=
        communityService.updateAnswerLikes(answerId, likes).await()

    suspend fun findProblemById(problemId:Int)=
        communityService.findProblemById(problemId).await()

    suspend fun answerProblem(content:String,time:String,userPhone:String,problemId: Int)=
        communityService.answerProblem(content, time, userPhone, problemId).await()

    suspend fun updateAnswer(answerId: Int,content:String,time:String)=
        communityService.updateAnswer(answerId,content,time).await()

    private suspend fun <T> Call<T>.await():T{
        return suspendCoroutine {continuation ->
            enqueue(object :Callback<T>{
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body=response.body()
                    if(body!=null){
                        continuation.resume(body)
                    }else{
                        continuation.resumeWithException(RuntimeException("response body is null"))
                    }
                }
            })
        }
    }

}