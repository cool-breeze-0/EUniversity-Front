package com.example.euniversity.network.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProblemAnswer(
    val problem:Problem,
    val answers:List<Answer>
):Parcelable