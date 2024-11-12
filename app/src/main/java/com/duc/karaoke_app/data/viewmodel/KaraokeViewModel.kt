package com.duc.karaoke_app.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duc.karaoke_app.data.model.YouTubeVideoItem

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class KaraokeViewModel(private val repository: KaraokeRepository) : ViewModel() {

    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> get() = _user
    private val _karaokeVideos = MutableLiveData<List<YouTubeVideoItem>>()
    val karaokeVideos: LiveData<List<YouTubeVideoItem>>
        get() = _karaokeVideos

    fun signInWithGoogle(idToken: String) {
        repository.firebaseAuthWithGoogle(idToken) { firebaseUser ->
            _user.value = firebaseUser
        }
    }

    fun searchKaraoke(apiKey: String) {
        viewModelScope.launch {
            val result = repository.searchKaraoke(apiKey)
            result.onSuccess { video ->
                _karaokeVideos.value = video
            }
        }
    }
}
