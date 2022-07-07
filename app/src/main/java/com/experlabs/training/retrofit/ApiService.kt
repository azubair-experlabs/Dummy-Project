package com.experlabs.training.retrofit


import com.experlabs.training.models.Data
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("/get_memes/")
    fun fetchMemes(): Call<Data?>
}