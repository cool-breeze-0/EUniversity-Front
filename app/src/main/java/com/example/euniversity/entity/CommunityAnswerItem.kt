package com.example.euniversity.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class CommunityAnswerItem(val answerProblemPersonNickname:String,val answerProblemPersonIdentity:String,
                          val answerProblemTime:String,val answerContent:String,val likeImage:Int,val likeQuantity:Int):Parcelable