package com.duc.karaoke_app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.duc.karaoke_app.R
import com.duc.karaoke_app.databinding.FragmentVideoPlayerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class VideoPlayerFragment : Fragment() {

    private lateinit var videPlayFragmentBinding: FragmentVideoPlayerBinding
    private lateinit var youtubePlayView: YouTubePlayerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        videPlayFragmentBinding= FragmentVideoPlayerBinding.inflate(layoutInflater)
        youtubePlayView = videPlayFragmentBinding.youtubePlayerView
        lifecycle.addObserver(youtubePlayView)
        val videoId = arguments?.getString("videoId")
        videoId?.let{
            youtubePlayView.addYouTubePlayerListener(object: AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            })
        }
        return videPlayFragmentBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        youtubePlayView.release()
    }

    companion object {
        fun newInstance(videoId: String): VideoPlayerFragment {
            val fragment = VideoPlayerFragment()
            val args = Bundle()
            args.putString("videoId", videoId)
            fragment.arguments = args
            return fragment
        }
    }

}