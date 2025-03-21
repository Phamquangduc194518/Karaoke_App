package com.duc.karaoke_app.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.MusicPlayerViewModel
import com.duc.karaoke_app.data.viewmodel.Repository
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.ViewModelHome
import com.duc.karaoke_app.databinding.FragmentWatchLiveBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView


class WatchLiveFragment : Fragment() {

    private lateinit var watchLiveBinding :FragmentWatchLiveBinding
    private val viewModel: MusicPlayerViewModel by activityViewModels{
        ViewModelFactory(Repository(), requireActivity().application)
    }
    private var exoPlayer: ExoPlayer? = null
    private val liveStreamUrl = "https://eb84952981fe.entrypoint.cloud.wowza.com/app-84Q7R43C/ngrp:a6501ead_all/playlist.m3u8"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        watchLiveBinding = FragmentWatchLiveBinding.inflate(layoutInflater)
        watchLiveBinding.viewModelWatchLive= viewModel
        watchLiveBinding.lifecycleOwner= viewLifecycleOwner
        return watchLiveBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        watchLiveBinding.rcvComments.layoutManager= LinearLayoutManager(requireContext())
        exoPlayer = ExoPlayer.Builder(requireContext()).build().also { player ->
            watchLiveBinding.playerView.player = player
            // Tạo MediaItem từ URL HLS
            val mediaItem = MediaItem.fromUri(Uri.parse(liveStreamUrl))
            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = true
        }
        viewModel.getCommentsByStream()
        viewModel.isStickerVideo.observe(viewLifecycleOwner){ isSticker->
            if(isSticker){
                val commentVideoFragment = StickerVideoFragment.newInstance()
                commentVideoFragment.show(childFragmentManager, commentVideoFragment.tag)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        releasePlayer()
    }

    private fun releasePlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }
}