package com.example.euniversity.utils

import android.content.Context
import android.net.ConnectivityManager

object NetworkStatus {
    fun getNetworkStatus(context: Context):Boolean{
        val connectivityManager:ConnectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val status=connectivityManager.activeNetworkInfo
        return if(status!=null && status.isConnected) true else false
    }
}