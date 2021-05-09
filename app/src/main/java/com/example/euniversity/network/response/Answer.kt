package com.example.euniversity.network.response

data class Answer(
    val id:Int,
    val content:String,
    val time:String,
    val likes:Int,
    val userPhone:String,
    val problemId:Int
)