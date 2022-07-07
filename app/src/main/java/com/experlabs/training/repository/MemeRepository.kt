package com.experlabs.training.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.experlabs.training.models.Data
import com.experlabs.training.models.Memelist
import com.experlabs.training.retrofit.RetrofitObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MemeRepository {

    private val memesLiveData = MutableLiveData<Memelist>()

    val memes : LiveData<Memelist>
    get() = memesLiveData


    fun getMemes(params: String, callback: (Boolean , String) -> Unit) {

        val retrofitCallback = object : Callback<Data?> {
            override fun onFailure(call: Call<Data?>, t: Throwable) {
                callback(false, t.toString().substringAfterLast(':'))
            }

            override fun onResponse(call: Call<Data?>, response: Response<Data?>) {
                response.body()?.memes?.let {
                    memesLiveData.postValue(it)
                }
                callback(true, "SUCCESS")
            }
        }

        RetrofitObject.getInstance().fetchMemes().enqueue(retrofitCallback)

    }
}