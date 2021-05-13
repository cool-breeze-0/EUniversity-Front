package com.example.euniversity.ui.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.euniversity.entity.CommunityProblemAnswerItem
import com.example.euniversity.network.response.ProblemAnswer

class CommunityViewModel : ViewModel() {
    var problemAnswerList=MutableLiveData<List<CommunityProblemAnswerItem>>()
    var problemIdList=MutableLiveData<ArrayList<Int>>()
}