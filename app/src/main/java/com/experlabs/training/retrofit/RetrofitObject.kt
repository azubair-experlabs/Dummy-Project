package com.experlabs.training.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitObject {

    var BASE_URL = "https://api.imgflip.com/"
    fun getInstance(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val instance = retrofit.create(ApiService::class.java)
        return instance
    }
}
