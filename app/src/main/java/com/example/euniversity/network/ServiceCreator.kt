package com.example.euniversity.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceCreator
{
    private const val BASE_URL="http://10.0.2.2:8080/"
    private val client = OkHttpClient.Builder().
        connectTimeout(3, TimeUnit.SECONDS).
        readTimeout(10, TimeUnit.SECONDS).
        writeTimeout(10, TimeUnit.SECONDS).build()
    fun getBASE_URL():String{
        return BASE_URL;
    }
    private val retrofit= Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T> create(serviceClass: Class<T>):T = retrofit.create(serviceClass)
    inline fun <reified T> create():T = create(T::class.java)
}