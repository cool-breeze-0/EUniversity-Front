package com.example.euniversity.network.response

data class ProblemAnswer(
    val problem:Problem,
    val answers:List<Answer>
)