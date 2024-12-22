package com.duc.karaoke_app.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LiveStreamViewModel(private val liveStreamRepository: LiveStreamRepository): ViewModel() {
    private val _isStreaming = MutableLiveData<Boolean>(false)
    val isStreaming : LiveData<Boolean>
        get() = _isStreaming
    private val _connectionStatus = MutableLiveData<String>()
    val connectionStatus: LiveData<String> get() = _connectionStatus

    init {
        liveStreamRepository.onConnectionSuccess = {
            _connectionStatus.postValue("Connection success")
        }
        liveStreamRepository.onConnectionFailed = { reason ->
            _connectionStatus.postValue("Connection failed: $reason")
            _isStreaming.postValue(false)
        }
        liveStreamRepository.onDisconnect = {
            _connectionStatus.postValue("Disconnected")
            _isStreaming.postValue(false)
        }
        liveStreamRepository.onAuthError = {
            _connectionStatus.postValue("Auth error")
        }
        liveStreamRepository.onAuthSuccess = {
            _connectionStatus.postValue("Auth success")
        }
    }

    fun prepareStream(): Boolean {
        return liveStreamRepository.prepareStream()
    }

    fun startStream(rtmpUrl: String) {
        liveStreamRepository.startStream(rtmpUrl)
        _isStreaming.value = true
    }

    fun stopStream() {
        liveStreamRepository.stopStream()
        _isStreaming.value = false
    }

    fun isStreaming(): Boolean {
        return liveStreamRepository.isStreaming()
    }

    fun startPreview() {
        liveStreamRepository.startPreview()
    }

    fun stopPreview() {
        liveStreamRepository.stopPreview()
    }

}