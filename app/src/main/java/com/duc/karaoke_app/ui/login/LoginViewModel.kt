package com.duc.karaoke_app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.duc.karaoke_app.data.LoginRepository

import com.duc.karaoke_app.R
import com.google.firebase.auth.FirebaseUser

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> get()= _user

    fun signInWithGoogle(idToken: String){
        loginRepository.firebaseAuthWithGoogle(idToken){ firebaseUser ->
            _user.value= firebaseUser
        }
    }
}