package com.experlabs.training.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.experlabs.training.models.Data
import com.experlabs.training.models.Meme
import com.experlabs.training.models.Memelist
import com.experlabs.training.retrofit.RetrofitObject
import com.experlabs.training.room.MemeDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException

class MemeRepository(private val retrofit: RetrofitObject, private val database: MemeDatabase) {

    private val memesLiveData = MutableLiveData<MutableList<Meme>>()

    val memes : LiveData<MutableList<Meme>>
    get() = memesLiveData

    fun getMemesFromApi(params: String, callback: (Boolean, String) -> Unit) {

        val retrofitCallback = object : Callback<Data?> {
            override fun onFailure(call: Call<Data?>, t: Throwable) {
                callback(false, t.toString().substringAfterLast(':'))
            }

            override fun onResponse(call: Call<Data?>, response: Response<Data?>) {
                response.body()?.memes?.memelist?.let {
                    memesLiveData.postValue(it as MutableList<Meme>?)
                    callback(true, "SUCCESS")
                }
            }
        }
        retrofit.getInstance().fetchMemes().enqueue(retrofitCallback)
    }

    suspend fun getMemesFromLocal(callback: (Boolean, String) -> Unit) {
        try {
            memesLiveData.postValue(database.dao()!!.getAll() as MutableList<Meme>?)
            callback(true, "Successfully Fetched")
        }
        catch (e : Throwable){
            callback(false, e.toString())
        }
    }

    suspend fun deleteFromLocal(meme : Meme, callback: (Boolean, String) -> Unit) {
        try {
            database.dao()!!.delete(meme)
            memesLiveData.value!!.remove(meme)
            callback(true, "Deleted Successfully")
        }
        catch (e : Throwable){
            callback(false, e.toString())
        }
    }

    suspend fun deleteAllFromLocal(callback: (Boolean, String) -> Unit) {
        try {
            database.dao()?.deleteAll()
            memesLiveData.value?.clear()
            callback(true, "Deleted All Successfully")
        }
        catch (e : Throwable){
            callback(false, e.toString())
        }
    }
}