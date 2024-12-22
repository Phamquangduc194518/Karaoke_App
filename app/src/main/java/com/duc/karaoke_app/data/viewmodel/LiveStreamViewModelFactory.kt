package com.duc.karaoke_app.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LiveStreamViewModelFactory(
    private val repository: LiveStreamRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LiveStreamViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LiveStreamViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}