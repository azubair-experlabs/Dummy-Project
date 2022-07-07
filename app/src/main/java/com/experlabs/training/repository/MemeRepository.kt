package com.experlabs.training.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.experlabs.training.models.Memelist
import com.experlabs.training.retrofit.ApiService
import com.experlabs.training.retrofit.RetrofitObject

class MemeRepository {

    private val memesLiveData = MutableLiveData<Memelist>()

    val memes : LiveData<Memelist>
    get() = memesLiveData

    suspend fun getMemes(){
        val api = RetrofitObject.getInstance().create(ApiService::class.java)
        val api_call = api.getMemes().body()?.let { it ->
            it.memes?.let { memelist ->
                memesLiveData.postValue(memelist)
            }
        }
    }
}