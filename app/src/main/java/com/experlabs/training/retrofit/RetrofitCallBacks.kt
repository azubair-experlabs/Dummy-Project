package com.experlabs.training.retrofit

interface RetrofitCallBacks {
    fun onResponse(flag: String)
    fun onFailure(t: Throwable)
}