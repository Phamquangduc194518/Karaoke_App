package com.duc.karaoke_app.data.viewmodel.favoriteSongsLocal

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.duc.karaoke_app.data.Repository.FavoriteSongsRepository
import androidx.lifecycle.ViewModel
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelHome

class FavoriteSongsViewModelFactory(
    private val application: Application,
    private val favoriteSongsRepository: FavoriteSongsRepository,
    private val viewModelHome: ViewModelHome
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavoriteSongsViewModel::class.java)) {
            FavoriteSongsViewModel(application, favoriteSongsRepository, viewModelHome) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}