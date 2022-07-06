package com.experlabs.training.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.experlabs.training.repository.MemeRepository

class MemeViewModelFactory(private val repository: MemeRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MemeViewModel(repository) as T
    }
}