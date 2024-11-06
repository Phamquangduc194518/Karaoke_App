package com.duc.karaoke_app

import android.os.Bundle

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.duc.karaoke_app.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var mainbinding: ActivityMainBinding
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainbinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainbinding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_bottom_fragment) as NavHostFragment
        navController= navHostFragment.navController
        bottomNavigation= mainbinding.bottomNavigation
        bottomNavigation.setupWithNavController(navController)
    }
}