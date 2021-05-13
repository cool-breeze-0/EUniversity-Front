package com.example.euniversity.network.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Answer(
    val id:Int,
    val content:String,
    val time:String,
    val likes:Int,
    val userPhone:String,
    val problemId:Int
):Parcelable