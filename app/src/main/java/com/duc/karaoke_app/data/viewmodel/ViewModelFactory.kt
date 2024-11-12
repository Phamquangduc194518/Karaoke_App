package com.duc.karaoke_app.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class ViewModelFactory(private val karaokeRepository: KaraokeRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KaraokeViewModel::class.java)) {
            return KaraokeViewModel(
                repository = KaraokeRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}