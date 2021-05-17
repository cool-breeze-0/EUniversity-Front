package com.example.euniversity.network.response

data class University(
    val id:Int,
    val name:String,
    val type:String,
    val level:String,
    val nature:String,
    val province:String,
    val city:String,
    val url:String,
    val logo:String,
    val introduction:String,
    val image:String,
    val f985:Int,
    val f211:Int,
    val dualClass:String
){
    constructor(id: Int,name: String,type: String,province: String,
    city: String,logo: String,f985: Int,f211: Int,dualClass: String):
            this(id, name, type, "", "", province, city, "", logo, "", "", f985, f211, dualClass)
}