package com.example.euniversity.network.response

data class Response<T>(
    val code:Int,
    val msg:String,
    val data:List<T>
)