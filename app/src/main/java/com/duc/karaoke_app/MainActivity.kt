package com.duc.karaoke_app

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelHome
import com.duc.karaoke_app.databinding.ActivityMainBinding
import com.duc.karaoke_app.ui.fragment.DuetSongFragment
import com.duc.karaoke_app.ui.fragment.HomeFragment
import com.duc.karaoke_app.ui.fragment.LearnFragment
import com.duc.karaoke_app.ui.fragment.LiveStreamFragment
import com.duc.karaoke_app.ui.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var mainbinding: ActivityMainBinding
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var viewModel: ViewModelHome
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val application = this.application
        val repository = Repository()
        val viewModelFactory = ViewModelFactory(repository,application)
        viewModel = ViewModelProvider(this, viewModelFactory)[ViewModelHome::class.java]
        mainbinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainbinding.root)
        viewModel.isDataLoaded.observe(this){
            if(viewModel.isDataLoaded.value == true){
                mainbinding.progressBar.visibility = View.GONE
                mainbinding.fragmentContainer.visibility = View.VISIBLE
                mainbinding.bottomNavigation.visibility = View.VISIBLE
            }
        }
        bottomNavigation= mainbinding.bottomNavigation
        loadFragment(HomeFragment())
        mainbinding.bottomNavigation.setOnItemSelectedListener{ item->
            when(item.itemId){
                R.id.navigation_home->{
                    loadFragment(HomeFragment())
                    true
                }
                R.id.navigation_song_ca->{
                    loadFragment(DuetSongFragment())
                    true
                }
                R.id.navigation_live->{
                    loadFragment(LiveStreamFragment())
                    true
                }
                R.id.navigation_learn->{
                    loadFragment(LearnFragment())
                    true
                }
                R.id.navigation_profile->{
                    loadFragment(ProfileFragment())
                    true
                }

                else -> {false}
            }
        }

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true){
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