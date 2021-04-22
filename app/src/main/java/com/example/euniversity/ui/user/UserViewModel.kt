package com.example.euniversity.ui.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.euniversity.entity.UserItem

class UserViewModel : ViewModel() {
    var userItems1= MutableLiveData<List<UserItem>>()
    var userItems2= MutableLiveData<List<UserItem>>()
}