package com.example.euniversity.network.response

data class User (
    val phone: String,
    val password: String,
    val nikeName: String,
    val gender:Int, //0表示男，1表示女
    val province: String,
    val city: String,
    val identity: String
)