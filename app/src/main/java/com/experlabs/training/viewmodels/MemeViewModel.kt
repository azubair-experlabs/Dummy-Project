package com.experlabs.training.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.experlabs.training.models.Memelist
import com.experlabs.training.repository.MemeRepository
import com.experlabs.training.retrofit.RetrofitCallBacks
import kotlinx.coroutines.launch

class MemeViewModel : ViewModel() , RetrofitCallBacks {

    private val repository = MemeRepository()

    fun getMemesFromRepository() {
        viewModelScope.launch {
            repository.getMemes(this@MemeViewModel)
        }
    }

    val memes : LiveData<Memelist>
    get() = repository.memes

    override fun onResponse(flag: String) {
        Log.i("OnResponse", flag)
    }

    override fun onFailure(t: Throwable) {
        Log.i("OnFailure", t.toString())
    }
}