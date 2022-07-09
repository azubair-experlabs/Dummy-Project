package com.experlabs.training.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.experlabs.training.models.Memelist
import com.experlabs.training.repository.MemeRepository
import kotlinx.coroutines.launch

class MemeViewModel(private val repository: MemeRepository) : ViewModel() {

    fun getMemesFromRepository(params : String, callback: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            repository.getMemes(params){flag, message ->
                response(flag, message, params, callback)
            }
        }
    }

    val memes : LiveData<Memelist>
    get() = repository.memes

    fun response(flag: Boolean, message: String, params: String, callback: (Boolean, String) -> Unit) {
        callback(flag, message)
        Log.i("Response", "$message $params")
    }
}