package com.duc.karaoke_app.feature_player.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.duc.karaoke_app.R
import com.duc.karaoke_app.core.utils.CloudinaryManager
import com.duc.karaoke_app.feature_favorite.data.FavoriteSongsRepository
import com.duc.karaoke_app.feature_home.data.Songs
import com.duc.karaoke_app.feature_home.data.Video
import com.duc.karaoke_app.feature_player.presentation.viewmodel.MusicPlayerViewModel
import com.duc.karaoke_app.feature_home.data.Repository
import com.duc.karaoke_app.feature_player.presentation.viewmodel.MusicPlayerViewModelFactory
import com.duc.karaoke_app.databinding.ActivityMusicPlayerBinding
import com.duc.karaoke_app.feature_player.presentation.ui.fragment.MusicFragment
import com.duc.karaoke_app.feature_player.presentation.ui.fragment.VideoPlayerFragment
import com.duc.karaoke_app.feature_player.presentation.ui.fragment.ViewDuetSongFragment
import com.duc.karaoke_app.feature_player.presentation.ui.fragment.WatchLiveFragment

class MusicPlayerActivity : AppCompatActivity() {
    private lateinit var musicPlayerBinding: ActivityMusicPlayerBinding
    private lateinit var viewModel : MusicPlayerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val application = this.application
        val repository= Repository()
        val favRepo = FavoriteSongsRepository(this)
        val viewModelFactory = MusicPlayerViewModelFactory(repository,favRepo,application)
        try {
            viewModel = ViewModelProvider(this, viewModelFactory)[MusicPlayerViewModel::class.java]
            Log.d("MusicPlayerActivity", "ViewModel initialized: $viewModel")
        } catch (e: Exception) {
            Log.e("MusicPlayerActivity", "Error initializing ViewModel: ${e.message}", e)
        }
        CloudinaryManager.init(getApplication())
        musicPlayerBinding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        musicPlayerBinding.musicPlayerViewModel= viewModel
        musicPlayerBinding.lifecycleOwner= this
        setContentView(musicPlayerBinding.root)

        val fragmentKey = intent.getStringExtra("FRAGMENT_KEY")
        when(fragmentKey){
            "Music_Fragment" -> loadFragment(MusicFragment())
            "Watch_Live_Fragment" -> loadFragment(WatchLiveFragment())
            "Duet_Song_Fragment" -> loadFragment(ViewDuetSongFragment())
            "Video_Fragment"->loadFragment(VideoPlayerFragment())
        }

        val song = intent.getParcelableExtra<Songs>("Play_List")
        if(song !=null){
            viewModel.setSong(song)
            Log.e("Bài hát nhận từ ViewModelHome","${song.title}")
        }

        val video = intent.getParcelableExtra<Video>("Video")
        if(video !=null){
            viewModel.setVideo(video)
            Log.e("dữ liệu video nhận từ VideModelHome","${video}")
        }

        val duetSong = intent.getParcelableExtra<Songs>("Duet_Song")
        if(duetSong !=null){
            viewModel.setSong(duetSong)
            viewModel.getDuetLyric()
            Log.e("dữ liệu nhận được duetSong","${duetSong.title}")
        }


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