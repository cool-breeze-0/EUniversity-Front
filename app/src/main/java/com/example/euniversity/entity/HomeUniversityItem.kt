package com.example.euniversity.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeUniversityItem(val universityImage:Int,val universityName:String,val universityAttribute:String):Parcelable