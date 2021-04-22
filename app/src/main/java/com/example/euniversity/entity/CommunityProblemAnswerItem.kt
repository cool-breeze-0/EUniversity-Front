package com.example.euniversity.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommunityProblemAnswerItem (val problem:String,val askProblemTime:String,val answer:String,
                                       val answerProblemTime:String):Parcelable