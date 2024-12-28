package com.duc.karaoke_app.data.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.duc.karaoke_app.data.model.Songs
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class MusicPlayerViewModel(private val repository: Repository, application: Application) : AndroidViewModel(application) {

    private val _song = MutableLiveData<Songs>()
    val song : LiveData<Songs>
        get()= _song

    private val _navigateBack= MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean>
        get()=_navigateBack

    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(application).build()

    private val _isPlaying = MutableLiveData<Boolean>(false)
    val isPlaying: LiveData<Boolean> get() = _isPlaying

    // Load and play song
    fun playSong(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
        _isPlaying.value = true
    }

    // Pause song
    fun pauseSong() {
        exoPlayer.pause()
        _isPlaying.value = false
    }

    // Stop and release player when not needed
    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }

    fun setSong(song: Songs){
        _song.value= song
        Log.d("MusicPlayerViewModel", "Song set: ${song.title}")
    }

    fun onBackPressed(){
        _navigateBack.value = true
    }


}
