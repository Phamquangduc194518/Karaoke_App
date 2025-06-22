package com.duc.karaoke_app.core.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.duc.karaoke_app.R
import com.duc.karaoke_app.feature_home.data.Repository
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelFactory
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelHome
import com.duc.karaoke_app.databinding.ActivityMainBinding
import com.duc.karaoke_app.feature_home.presentation.ui.fragment.DuetSongFragment
import com.duc.karaoke_app.feature_home.presentation.ui.fragment.HomeFragment
import com.duc.karaoke_app.feature_home.presentation.ui.fragment.LearnFragment
import com.duc.karaoke_app.feature_home.presentation.ui.fragment.LiveStreamFragment
import com.duc.karaoke_app.feature_home.presentation.ui.fragment.ProfileFragment
import com.duc.karaoke_app.feature_home.presentation.ui.fragment.VipRequiredFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var mainbinding: ActivityMainBinding
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var viewModel: ViewModelHome
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val application = this.application
        val repository = Repository()
        val viewModelFactory = ViewModelFactory(repository, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[ViewModelHome::class.java]
        mainbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainbinding.root)
        viewModel.isDataLoaded.observe(this) { isLoaded ->
            if (isLoaded == true) {
                mainbinding.rocketAnimation.visibility = View.VISIBLE
                mainbinding.fragmentContainer.visibility = View.GONE
                mainbinding.bottomNavigation.visibility = View.GONE
                mainbinding.rocketAnimation.playAnimation()
                lifecycleScope.launch {
                    delay(3000)
                    mainbinding.rocketAnimation.animate()
                        .alpha(0f)
                        .setDuration(500)
                        .withEndAction {
                            mainbinding.rocketAnimation.cancelAnimation()
                            mainbinding.rocketAnimation.visibility = View.GONE
                            mainbinding.rocketAnimation.alpha = 1f
                            mainbinding.fragmentContainer.alpha = 0f
                            mainbinding.bottomNavigation.alpha = 0f
                            mainbinding.fragmentContainer.visibility = View.VISIBLE
                            mainbinding.bottomNavigation.visibility = View.VISIBLE
                            mainbinding.fragmentContainer.animate().alpha(1f).setDuration(500).start()
                            mainbinding.bottomNavigation.animate().alpha(1f).setDuration(500).start()
                        }.start()
                }
            }
        }
        bottomNavigation = mainbinding.bottomNavigation
        loadFragment(HomeFragment())
        mainbinding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(HomeFragment())
                    true
                }

                R.id.navigation_song_ca -> {
                    loadFragment(DuetSongFragment())
                    true
                }

                R.id.navigation_live -> {
                    if (viewModel.userProfile.value?.role == "vip") {
                        loadFragment(LiveStreamFragment())
                    } else {
                        loadFragment(VipRequiredFragment())
                    }
                    true
                }

                R.id.navigation_learn -> {
                    loadFragment(LearnFragment())
                    true
                }

                R.id.navigation_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }

                else -> {
                    false
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack() // Quay lại Fragment trước đó
                } else {
//                    moveTaskToBack(true) // Đưa app về background thay vì thoát
                }
            }
        })
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

}