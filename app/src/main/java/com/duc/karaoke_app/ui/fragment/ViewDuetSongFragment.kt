package com.duc.karaoke_app.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.duc.karaoke_app.data.viewmodel.musicPlayer.MusicPlayerViewModel
import com.duc.karaoke_app.data.Repository.Repository
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentViewDuetSongBinding

class ViewDuetSongFragment : Fragment() {
    private lateinit var viewDuetSongBinding: FragmentViewDuetSongBinding
    private val handler = Handler(Looper.getMainLooper())
    private var currentTime = 0 // Thời gian giả lập (giây)
    private val viewModel: MusicPlayerViewModel by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDuetSongBinding = FragmentViewDuetSongBinding.inflate(layoutInflater)
        viewDuetSongBinding.viewModelViewDuetSong = viewModel
        viewDuetSongBinding.lifecycleOwner = viewLifecycleOwner
        return viewDuetSongBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startLyricTracking()
    }

    private fun startLyricTracking() {
        handler.post(object : Runnable {
            override fun run() {
                viewModel.viewDuetSongAdapter.updateCurrentLyric(currentTime) // Cập nhật lyric
                viewModel.viewDuetSongAdapter.scrollToCurrentLyric(viewDuetSongBinding.rcvViewDuetSong) // Cuộn xuống lyric mới

                currentTime++ // Tăng thời gian mỗi giây
                handler.postDelayed(this, 1000) // Chạy lại sau 1 giây
            }
        })
    }
}