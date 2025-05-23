package com.duc.karaoke_app.data.viewmodel.messenger

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duc.karaoke_app.data.Repository.ChatRepository

class ViewModelChatFactory(private val repository: ChatRepository, private val application: Application,): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelChat::class.java)) {
            return ViewModelChat(repository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}