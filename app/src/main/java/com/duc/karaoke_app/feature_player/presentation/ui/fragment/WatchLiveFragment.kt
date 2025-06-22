package com.duc.karaoke_app.feature_player.presentation.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.feature_player.presentation.viewmodel.MusicPlayerViewModel
import com.duc.karaoke_app.feature_home.data.Repository
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentWatchLiveBinding
import com.duc.karaoke_app.feature_chat.data.remote.LiveStreamSocketManager
import com.duc.karaoke_app.feature_home.presentation.ui.adapter.WatchLiveAdapter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem


class WatchLiveFragment : Fragment() {

    private lateinit var watchLiveBinding :FragmentWatchLiveBinding
    private val viewModel: MusicPlayerViewModel by activityViewModels{
        ViewModelFactory(Repository(), requireActivity().application)
    }
    private var exoPlayer: ExoPlayer? = null
    private val liveStreamUrl = "https://7e2750f951fd.entrypoint.cloud.wowza.com/app-6tfRw2M4/ngrp:acac9c4e_all/playlist.m3u8"
    private lateinit var watchLiveAdapter: WatchLiveAdapter

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
        watchLiveAdapter = WatchLiveAdapter()
        viewModel.isLiveId.observe(viewLifecycleOwner) { streamId ->
            if (streamId != null && streamId != 0) {
                LiveStreamSocketManager.init(
                    streamId = streamId,
                    authToken = viewModel._toKenToMusicPlayer
                )
                LiveStreamSocketManager.connect()
                LiveStreamSocketManager.listenNewComment { json ->
                    watchLiveAdapter.addNewComment(json)
                }
            } else {
                Log.e("LiveSocket", "Chưa có streamId hợp lệ!")
            }
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
        LiveStreamSocketManager.disconnect()
    }

    private fun releasePlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }
}