package com.duc.karaoke_app.feature_splash_screen.ui

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.duc.karaoke_app.databinding.ActivitySplashScreenBinding
import com.duc.karaoke_app.feature_auth.presentation.ui.LoginActivity
import com.duc.karaoke_app.feature_splash_screen.ui.adapter.OnboardingViewPagerAdapter
import com.duc.karaoke_app.feature_splash_screen.viewmodel.ViewModelSplashScreen
import com.google.android.material.tabs.TabLayoutMediator
import com.duc.karaoke_app.R
import com.duc.karaoke_app.core.utils.PermissionUtils
import com.duc.karaoke_app.feature_splash_screen.ui.fragment.OnboardingPermissionGrantedCallback
import com.duc.karaoke_app.feature_splash_screen.ui.fragment.SplashScreenFragment

class SplashScreenActivity : AppCompatActivity(), OnboardingPermissionGrantedCallback {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var viewModel: ViewModelSplashScreen
    private lateinit var adapter: OnboardingViewPagerAdapter
    private val PREF_NAME = "karaoke_pref"
    private val KEY_ONBOARDING_COMPLETE = "onboarding_complete"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[ViewModelSplashScreen::class.java]
        val prefs = getSharedPreferences("karaoke_pref", MODE_PRIVATE)
        val hasShownOnboarding = prefs.getBoolean("onboarding_complete", false)
        val permissionsGranted = PermissionUtils.areAllRequiredPermissionsGranted(this)

        if (hasShownOnboarding || permissionsGranted) {
            showSplashScreenFragment()
        } else {
            showOnboarding()
        }

    }

    private fun observeViewModel() {
        viewModel.currentPage.observe(this) { page ->
            binding.viewPager.currentItem = page
        }

        viewModel.navigateToLogin.observe(this) { shouldNavigate ->
            if (shouldNavigate) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                viewModel.resetNavigation()
            }
        }
    }


    private fun showOnboarding() {
        binding.rltIntroScreen.visibility = View.VISIBLE
        binding.fragmentContainerSplashScreen.visibility = View.GONE
        adapter = OnboardingViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.pageIndicator, binding.viewPager) { _, _ -> }.attach()

        binding.btnNextStep.setOnClickListener {
            viewModel.onNextPageClicked()
        }

        binding.textSkip.setOnClickListener {
            viewModel.onSkipClicked()
        }

        observeViewModel()
    }

    private fun showSplashScreenFragment() {
        binding.fragmentContainerSplashScreen.visibility = View.VISIBLE
        binding.rltIntroScreen.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_splash_screen, SplashScreenFragment())
            .commit()
    }

    override fun onAllPermissionsGranted() {
        getSharedPreferences("karaoke_pref", MODE_PRIVATE)
            .edit()
            .putBoolean("onboarding_complete", true)
            .apply()
        showSplashScreenFragment()
    }

    private fun allPermissionsGranted(): Boolean {
        return PermissionUtils.areAllRequiredPermissionsGranted(this)
    }
}
