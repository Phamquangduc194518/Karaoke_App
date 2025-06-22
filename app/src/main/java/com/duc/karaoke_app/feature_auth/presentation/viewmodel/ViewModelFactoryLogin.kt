package com.duc.karaoke_app.feature_auth.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duc.karaoke_app.feature_auth.data.AuthRepository
import com.duc.karaoke_app.feature_chat.data.ChatRepository
import com.duc.karaoke_app.feature_chat.presentation.viewmodel.ViewModelChat

class ViewModelFactoryLogin(private val repository: AuthRepository, private val application: Application):  ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelLogin::class.java)) {
            return ViewModelLogin(repository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}