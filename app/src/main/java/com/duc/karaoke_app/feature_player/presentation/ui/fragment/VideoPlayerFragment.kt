package com.duc.karaoke_app.feature_player.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.feature_player.presentation.viewmodel.MusicPlayerViewModel
import com.duc.karaoke_app.feature_home.data.Repository
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentVideoPlayerBinding

class VideoPlayerFragment : Fragment() {

    private lateinit var videPlayFragmentBinding: FragmentVideoPlayerBinding
    private val viewModel: MusicPlayerViewModel by activityViewModels{
        ViewModelFactory(Repository(), requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        videPlayFragmentBinding= FragmentVideoPlayerBinding.inflate(layoutInflater)
        videPlayFragmentBinding.viewModelVideoPlayer= viewModel
        videPlayFragmentBinding.lifecycleOwner= viewLifecycleOwner
        return videPlayFragmentBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        videPlayFragmentBinding.rcvVideo.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getCommentVideo()

        viewModel.isStickerVideo.observe(viewLifecycleOwner){ isSticker->
            if(isSticker){
                val commentVideoFragment = StickerVideoFragment.newInstance()
                commentVideoFragment.show(childFragmentManager, commentVideoFragment.tag)
            }
        }
    }



}