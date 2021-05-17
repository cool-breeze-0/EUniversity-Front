package com.example.euniversity.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeUniversityItem(val universityLogo:String,val universityName:String,val universityAttribute:String):Parcelable