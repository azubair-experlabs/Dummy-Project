package com.experlabs.training.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.experlabs.training.models.Meme
import com.experlabs.training.repository.MemeRepository
import kotlinx.coroutines.launch

class MemeViewModel(private val repository: MemeRepository) : ViewModel() {

    fun getMemesFromRepository(params : String, fromApi : Boolean, callback: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            if (fromApi) {
                repository.getMemesFromApi(params) { flag, message ->
                    response(flag, message, params, callback)
                }
            }
            else{
                repository.getMemesFromLocal(){ flag, message ->
                    response(flag, message, params, callback)
                }
            }
        }
    }

    fun deleteMemeFromRepository(meme: Meme, callback: (Boolean, String) -> Unit){
        viewModelScope.launch {
            repository.deleteFromLocal(meme){ flag, message ->
                response(flag, message, callback = callback)
            }
        }
    }

    fun deleteAllMemesFromRepository(callback: (Boolean, String) -> Unit){
        viewModelScope.launch {
            repository.deleteAllFromLocal(){ flag, message ->
                response(flag, message, callback = callback)
            }
        }
    }

    val memes : LiveData<MutableList<Meme>>
    get() = repository.memes

    fun response(flag: Boolean, message: String, params: String = "", callback: (Boolean, String) -> Unit) {
        callback(flag, message)
        Log.i("Response", "$message $params")
    }
}