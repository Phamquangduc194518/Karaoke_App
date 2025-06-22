package com.duc.karaoke_app.feature_auth.presentation.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.duc.karaoke_app.R
import com.duc.karaoke_app.feature_auth.data.AuthRepository
import com.duc.karaoke_app.feature_auth.presentation.viewmodel.ViewModelFactoryLogin
import com.duc.karaoke_app.feature_auth.presentation.viewmodel.ViewModelLogin


class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: ViewModelLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        val application = this.application
        val repository = AuthRepository()
        val viewModelFactory = ViewModelFactoryLogin(repository,application)
        viewModel = ViewModelProvider(this, viewModelFactory)[ViewModelLogin::class.java]
        setContentView(R.layout.activity_login)
    }

}