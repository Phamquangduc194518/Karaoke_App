package com.duc.karaoke_app.feature_home.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duc.karaoke_app.feature_home.data.Repository
import com.duc.karaoke_app.feature_auth.presentation.viewmodel.ViewModelLogin

class ViewModelFactory(
    private val repository: Repository,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Log.d("ViewModelFactory", "Creating ViewModel for class: ${modelClass.name}")

        return when {
            modelClass.isAssignableFrom(ViewModelHome::class.java) -> {
                Log.d("ViewModelFactory", "Creating ViewModelLogin")
                ViewModelHome(repository, application) as T
            }
            else -> {
                Log.e("ViewModelFactory", "Unknown ViewModel class: ${modelClass.name}")
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}


