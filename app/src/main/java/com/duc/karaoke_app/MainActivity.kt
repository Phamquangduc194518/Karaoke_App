package com.duc.karaoke_app

import android.os.Bundle

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.duc.karaoke_app.utils.GoogleSignInHelper
import com.duc.karaoke_app.databinding.ActivityMainBinding
import com.duc.karaoke_app.ui.fragment.DuetFragment
import com.duc.karaoke_app.ui.fragment.FavouriteFragment
import com.duc.karaoke_app.ui.fragment.HomeFragment
import com.duc.karaoke_app.ui.fragment.LiveFragment
import com.duc.karaoke_app.ui.fragment.LiveStreamFragment
import com.duc.karaoke_app.ui.fragment.ProfileFragment
import com.duc.karaoke_app.ui.fragment.VideoPlayerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var mainbinding: ActivityMainBinding
    private lateinit var bottomNavigation: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainbinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainbinding.root)
        bottomNavigation= mainbinding.bottomNavigation
        GoogleSignInHelper.initialize(this)
        loadFragment(HomeFragment())
        mainbinding.bottomNavigation.setOnItemSelectedListener{ item->
            when(item.itemId){
                R.id.navigation_home->{
                    loadFragment(HomeFragment())
                    true
                }
                R.id.navigation_favourite->{
                    loadFragment(FavouriteFragment())
                    true
                }
                R.id.navigation_live->{
                    loadFragment(LiveStreamFragment())
                    true
                }
                R.id.navigation_duet->{
                    loadFragment(DuetFragment())
                    true
                }
                R.id.navigation_profile->{
                    loadFragment(ProfileFragment())
                    true
                }

                else -> {false}
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

}