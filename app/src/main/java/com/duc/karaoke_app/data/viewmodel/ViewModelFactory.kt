package com.duc.karaoke_app.data.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(
    private val repository: Repository,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Log.d("ViewModelFactory", "Creating ViewModel for class: ${modelClass.name}")

        return when {
            modelClass.isAssignableFrom(ViewModelLogin::class.java) -> {
                Log.d("ViewModelFactory", "Creating ViewModelLogin2")
                ViewModelLogin(repository, application) as T
            }
            modelClass.isAssignableFrom(ViewModelHome::class.java) -> {
                Log.d("ViewModelFactory", "Creating ViewModelLogin")
                ViewModelHome(repository, application) as T
            }
            modelClass.isAssignableFrom(MusicPlayerViewModel::class.java) -> {
                Log.d("ViewModelFactory", "Creating MusicPlayerViewModel")
                MusicPlayerViewModel(repository, application) as T
            }
            else -> {
                Log.e("ViewModelFactory", "Unknown ViewModel class: ${modelClass.name}")
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}


