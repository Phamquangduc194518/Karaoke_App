package com.duc.karaoke_app.feature_home.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.feature_home.data.Repository
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentVideoByTopicBinding
import com.duc.karaoke_app.feature_home.presentation.viewmodel.ViewModelHome
import com.duc.karaoke_app.feature_player.presentation.ui.MusicPlayerActivity

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

        binding.ivBack.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }

    }

}