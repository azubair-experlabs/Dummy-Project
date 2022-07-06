package com.experlabs.training.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.experlabs.training.models.Memelist
import com.experlabs.training.repository.MemeRepository
import kotlinx.coroutines.launch

class MemeViewModel(private val repository: MemeRepository) : ViewModel(){

    init {
        viewModelScope.launch {
            repository.getMemes()
        }
    }

    val memes : LiveData<Memelist>
    get() = repository.memes
}