package com.duc.karaoke_app.feature_splash_screen.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class ViewModelSplashScreen : ViewModel() {
    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    private val _navigateToRegister = MutableLiveData<Boolean>()
    val navigateToRegister: LiveData<Boolean>
        get()=_navigateToRegister

    private val _currentPage = MutableLiveData(0)
    val currentPage: LiveData<Int> get() = _currentPage

    val totalPage = 3

    fun onNextPageClicked() {
        if ((_currentPage.value ?: 0) < totalPage - 1) {
            _currentPage.value = (_currentPage.value ?: 0) + 1
        } else {
            _navigateToLogin.value = true
        }
    }

    fun onSkipClicked() {
        _navigateToLogin.value = true
    }

    fun onSplashScreenClick() {
        Log.d("SplashScreen", "Splash Screen clicked")
        _navigateToLogin.value = true
    }

    fun onSplashScreenClickToRegister() {
        Log.d("SplashScreen", "Splash Screen clicked")
        _navigateToRegister.value = true
    }

    fun resetNavigation() {
        _navigateToLogin.value = false
        _navigateToRegister.value=false
    }
}