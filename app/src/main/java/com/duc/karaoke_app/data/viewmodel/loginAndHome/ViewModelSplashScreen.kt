package com.duc.karaoke_app.data.viewmodel.loginAndHome


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