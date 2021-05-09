package com.example.euniversity.network.response

data class Like(
    val userPhone:String,
    val answerId:Int,
    val like:Boolean
)