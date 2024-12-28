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
import com.google.android.exoplayer2.Player

class MusicPlayerViewModel(private val repository: Repository, application: Application) : AndroidViewModel(application) {

    private val _song = MutableLiveData<Songs>()
    val song: LiveData<Songs> get() = _song

    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(application).build()

    private val _isPlaying = MutableLiveData<Boolean>(false)
    val isPlaying: LiveData<Boolean> get() = _isPlaying

    private val _currentPosition = MutableLiveData<Long>(0L)
    val currentPosition: LiveData<Long> get() = _currentPosition

    private val _duration = MutableLiveData<Long>(0L)
    val duration: LiveData<Long> get() = _duration

    private val _isSeekbarTracking = MutableLiveData<Boolean>(false)
    val isSeekbarTracking: LiveData<Boolean> get() = _isSeekbarTracking

    private val _navigateBack = MutableLiveData<Boolean>(false)
    val navigateBack: LiveData<Boolean> get() = _navigateBack

    fun onBackPressed() {
        _navigateBack.value = true
    }

    fun setSong(song: Songs) {
        _song.value = song
        Log.d("MusicPlayerViewModel", "Song set: ${song.title}")
    }


    private val updateSeekbarRunnable = object : Runnable {
        override fun run() {
            if (_isSeekbarTracking.value == false) {
                _currentPosition.postValue(exoPlayer.currentPosition)
            }
            // Lặp lại cập nhật mỗi 500ms
            android.os.Handler().postDelayed(this, 500)
        }
    }

    init {
        // Lắng nghe trạng thái bài hát
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    _duration.value = exoPlayer.duration
                }
            }
        })

        // Bắt đầu cập nhật SeekBar
        android.os.Handler().post(updateSeekbarRunnable)
    }

    fun playSong(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
        _isPlaying.value = true
    }

    fun pauseSong() {
        exoPlayer.pause()
        _isPlaying.value = false
    }

    fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
    }

    fun setSeekbarTracking(isTracking: Boolean) {
        _isSeekbarTracking.value = isTracking
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }
}

