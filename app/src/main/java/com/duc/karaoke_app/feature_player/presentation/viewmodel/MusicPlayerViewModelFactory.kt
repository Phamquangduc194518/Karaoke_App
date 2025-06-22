package com.duc.karaoke_app.feature_player.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duc.karaoke_app.feature_favorite.data.FavoriteSongsRepository
import com.duc.karaoke_app.feature_home.data.Repository

class MusicPlayerViewModelFactory(
    private val repo: Repository,
    private val favRepo: FavoriteSongsRepository,
    private val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MusicPlayerViewModel(repo, favRepo, application) as T
    }
}
