package com.experlabs.training.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.experlabs.training.models.Data
import com.experlabs.training.models.Memelist
import com.experlabs.training.retrofit.RetrofitCallBacks
import com.experlabs.training.retrofit.RetrofitObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MemeRepository {

    private val memesLiveData = MutableLiveData<Memelist>()

    val memes : LiveData<Memelist>
    get() = memesLiveData


    fun getMemes(callback: RetrofitCallBacks) {

        val retrofitCallback = object : Callback<Data?> {
            override fun onFailure(call: Call<Data?>, t: Throwable) {
                callback.onFailure(t)
            }

            override fun onResponse(call: Call<Data?>, response: Response<Data?>) {
                response.body()?.memes?.let {
                    memesLiveData.postValue(it)
                }
                callback.onResponse("SUCCESS")
            }
        }

        RetrofitObject.getInstance().fetchMemes().enqueue(retrofitCallback)

    }
}