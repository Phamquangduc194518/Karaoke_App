package com.duc.karaoke_app

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.duc.karaoke_app.data.Repository.Repository
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelLogin

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: ViewModelLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        val application = this.application
        val repository = Repository()
        val viewModelFactory = ViewModelFactory(repository,application)
        viewModel = ViewModelProvider(this, viewModelFactory)[ViewModelLogin::class.java]
        setContentView(R.layout.activity_login)
    }

}