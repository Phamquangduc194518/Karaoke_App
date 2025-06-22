package com.duc.karaoke_app.feature_splash_screen.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.duc.karaoke_app.R
import com.duc.karaoke_app.databinding.FragmentSplashScreenBinding
import com.duc.karaoke_app.feature_auth.presentation.ui.LoginActivity
import com.duc.karaoke_app.feature_splash_screen.viewmodel.ViewModelSplashScreen


class SplashScreenFragment : Fragment() {

    private lateinit var splashScreenBinding: FragmentSplashScreenBinding
    private lateinit var viewModel: ViewModelSplashScreen
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        splashScreenBinding = FragmentSplashScreenBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[ViewModelSplashScreen::class.java]
        splashScreenBinding.viewModelSplashScreen = viewModel
        splashScreenBinding.lifecycleOwner = this
        viewModel.navigateToLogin.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
                viewModel.resetNavigation()
            }
        }

        return splashScreenBinding.root
    }
}