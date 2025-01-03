package com.duc.karaoke_app

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.duc.karaoke_app.data.model.Songs
import com.duc.karaoke_app.data.viewmodel.MusicPlayerViewModel
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.databinding.ActivityMusicPlayerBinding
import com.duc.karaoke_app.ui.fragment.MusicFragment

class MusicPlayerActivity : AppCompatActivity() {
    private lateinit var musicPlayerBinding: ActivityMusicPlayerBinding
    private lateinit var viewModel : MusicPlayerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val application = this.application
        val repository= Repository()
        val viewModelFactory = ViewModelFactory(repository,application)
        try {
            Log.d("MusicPlayerActivity", "Initializing ViewModelProvider")
            viewModel = ViewModelProvider(this, viewModelFactory)[MusicPlayerViewModel::class.java]
            Log.d("MusicPlayerActivity", "ViewModel initialized: $viewModel")
        } catch (e: Exception) {
            Log.e("MusicPlayerActivity", "Error initializing ViewModel: ${e.message}", e)
        }
        musicPlayerBinding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        musicPlayerBinding.musicPlayerViewModel= viewModel
        musicPlayerBinding.lifecycleOwner= this
        setContentView(musicPlayerBinding.root)

        val song = intent.getParcelableExtra<Songs>("song_data")
        Log.e("dữ liệu nhận được lần 1","${song}")
        if(song !=null){
            viewModel.setSong(song)
            Log.e("dữ liệu nhận được lần 2","${song.title}")
        }
        loadFragment(MusicFragment())

        // Quan sát sự kiện "Quay lại"
        viewModel.navigateBack.observe(this) { shouldNavigateBack ->
            if (shouldNavigateBack) {
                finish() // Hủy Activity và quay lại ActivityA
            }
        }

    }
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_music_player, fragment)
            .commit()
    }


}