package com.duc.karaoke_app.data.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duc.karaoke_app.data.model.RecordedSongs
import com.duc.karaoke_app.data.model.Songs
import com.duc.karaoke_app.data.network.DriveUploader
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.api.client.http.FileContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import kotlinx.coroutines.launch

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

    private var _toKenToMusicPlayer = ""

    var userId = MutableLiveData<Int>()
    var songName = MutableLiveData("")
    var recordingPath = MutableLiveData("")

    init{
        saveTokenToMusicPlayerActivity()
    }

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
    fun togglePlayPause(url: String) {
        if (_isPlaying.value == true) {
            pauseSong()
        } else {
            if (exoPlayer.currentMediaItem == null || exoPlayer.mediaItemCount == 0) {
                // Nếu chưa có MediaItem, thêm và phát nhạc
                playSong(url)
            } else {
                // Nếu đã có MediaItem, tiếp tục phát
                exoPlayer.play()
                _isPlaying.value = true
            }
        }
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

    private fun saveTokenToMusicPlayerActivity() : String?{
        val sharedPreferences=getApplication<Application>().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)
        if(token != null){
            Log.e("Token To MusicPlayer","$token")
            _toKenToMusicPlayer= token.trim()
        }else{
            Log.e("Token To MusicPlayer","Token không tồn tại")
        }
        return _toKenToMusicPlayer
    }

    fun createRecordedSongs(){
        viewModelScope.launch {
            val request = RecordedSongs(
                userId = userId.value ?: 0,
                songName = songName.value ?: "",
                recordingPath= recordingPath.value ?: "")
            try{
                val response = repository.createRecordedSong(_toKenToMusicPlayer,request)
                if(response.isSuccessful){
                    val apiResponse = response.body()
                    Log.e("Tạo bản ghi thành công","$apiResponse")
                }else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Bản ghi", "Lỗi: ${response.code()} - $errorBody")
                }
            }catch(e: Exception){
                Log.e("bản ghi", "Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun uploadFile(localFile: java.io.File, folderId: String) {
        viewModelScope.launch {
            val fileId = DriveUploader.uploadFile(localFile, folderId)
            if (fileId != null) {
                // Handle success, e.g., update UI
                Log.d("DriveViewModel", "File uploaded successfully. File ID: $fileId")
            } else {
                // Handle failure, e.g., show error message
                Log.e("DriveViewModel", "File upload failed.")
            }
        }
    }

}

