package com.experlabs.training.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.experlabs.training.models.Memelist
import com.experlabs.training.retrofit.ApiService

class MemeRepository(private val api_service : ApiService) {

    private val memesLiveData = MutableLiveData<Memelist>()

    val memes : LiveData<Memelist>
    get() = memesLiveData

    suspend fun getMemes(){
        val api_call = api_service.getMemes().body()?.let { it ->
            it.memes?.let { memelist ->
                memesLiveData.postValue(memelist)
            }
        }
    }
}