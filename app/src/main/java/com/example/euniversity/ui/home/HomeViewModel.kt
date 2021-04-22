package com.example.euniversity.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.euniversity.entity.HomeUniversityItem

class HomeViewModel : ViewModel() {

    var universityList=MutableLiveData<List<HomeUniversityItem>>()
}