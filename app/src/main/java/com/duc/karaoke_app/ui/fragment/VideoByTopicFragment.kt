package com.duc.karaoke_app.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.MusicPlayerActivity
import com.duc.karaoke_app.data.Repository.Repository
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelHome
import com.duc.karaoke_app.databinding.FragmentVideoByTopicBinding

class VideoByTopicFragment : Fragment() {
    private lateinit var binding: FragmentVideoByTopicBinding
    private val viewModel: ViewModelHome by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoByTopicBinding.inflate(layoutInflater)
        binding.viewModelVideoOfTopic = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcvVideos.layoutManager= LinearLayoutManager(requireContext())
        viewModel.getAllVideoOfTopic()
        viewModel.selectedVideo.observe(viewLifecycleOwner) { video ->
            if (video != null) {
                val intent = Intent(requireContext(), MusicPlayerActivity::class.java).apply {
                    putExtra("FRAGMENT_KEY", "Video_Fragment")
                    putExtra("Video", video)
                }
                startActivity(intent)
            }
        }

    }

}