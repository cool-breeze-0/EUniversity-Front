package com.example.euniversity.ui.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserRetrievePasswordViewModel : ViewModel() {
    var code= MutableLiveData<String>()
}