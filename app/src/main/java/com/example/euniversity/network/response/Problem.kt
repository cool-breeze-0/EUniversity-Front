package com.example.euniversity.network.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Problem(
    val id:Int,
    val content:String,
    val time:String,
    val userPhone:String
): Parcelable