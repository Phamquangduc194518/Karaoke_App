package com.duc.karaoke_app

import android.graphics.Color
import android.os.Bundle

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.duc.karaoke_app.databinding.ActivityMainBinding
import com.duc.karaoke_app.ui.fragment.NewsFeed
import com.duc.karaoke_app.ui.fragment.SingleSingerFragment
import com.duc.karaoke_app.ui.fragment.HomeFragment
import com.duc.karaoke_app.ui.fragment.LiveStreamFragment
import com.duc.karaoke_app.ui.fragment.ProfileFragment
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
        loadFragment(HomeFragment())
        mainbinding.bottomNavigation.setOnItemSelectedListener{ item->
            when(item.itemId){
                R.id.navigation_home->{
                    loadFragment(HomeFragment())
                    true
                }
                R.id.navigation_song_ca->{
                    loadFragment(SingleSingerFragment())
                    true
                }
                R.id.navigation_live->{
                    loadFragment(LiveStreamFragment())
                    val drawable = ContextCompat.getDrawable(this, R.drawable.live_after_selected)
                    drawable?.setTint(Color.parseColor("#FF0000")) // Chuyển đổi từ chuỗi sang màu đỏ
                    item.icon = drawable
                    true
                }
                R.id.navigation_learn->{
                    loadFragment(NewsFeed())
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