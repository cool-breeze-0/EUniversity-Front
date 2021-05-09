package com.example.euniversity.network.response

data class UniversityScore(
    val universityId:Int,
    val birthplace:String,
    val division:String,
    val year:String,
    val grade:Int
)