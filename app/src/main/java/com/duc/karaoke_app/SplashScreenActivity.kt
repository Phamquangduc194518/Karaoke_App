package com.duc.karaoke_app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.duc.karaoke_app.data.viewmodel.ViewModelSplashScreen
import com.duc.karaoke_app.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var splashScreenBinding: ActivitySplashScreenBinding
    private lateinit var viewModel: ViewModelSplashScreen
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        splashScreenBinding= ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(splashScreenBinding.root)

        viewModel = ViewModelProvider(this)[ViewModelSplashScreen::class.java]

        splashScreenBinding.viewModelSplashScreen= viewModel
        splashScreenBinding.lifecycleOwner= this

        viewModel.navigateToLogin.observe(this){ shouldNavigate->
            if(shouldNavigate){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                viewModel.resetNavigation()
            }
        }

    }
}