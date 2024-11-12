package com.duc.karaoke_app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.viewmodel.KaraokeRepository
import com.duc.karaoke_app.data.viewmodel.KaraokeViewModel
import com.duc.karaoke_app.data.viewmodel.ViewModelFactory
import com.duc.karaoke_app.databinding.FragmentHomeBinding
import com.duc.karaoke_app.ui.adapter.YouTubeAdapter

class HomeFragment : Fragment() {

    private lateinit var homeBinding: FragmentHomeBinding
    private val viewModel: KaraokeViewModel by viewModels { ViewModelFactory(KaraokeRepository()) }
    private lateinit var adapter: YouTubeAdapter
    val apiKey = "AIzaSyD7L2AC6LciuwBHR9qWt3QQI4qf1gH3Plg"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.karaokeVideos.observe(viewLifecycleOwner, Observer { video ->
            if (video.isNotEmpty()) {
                homeBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                adapter = YouTubeAdapter(video) { videoId ->
                    openVideoPlayer(videoId)
                }
                homeBinding.recyclerView.adapter = adapter

            }
        })
        viewModel.searchKaraoke(apiKey)


    }

    private fun openVideoPlayer(videoId: String) {
        val videoPlayerFragment = VideoPlayerFragment.newInstance(videoId)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, videoPlayerFragment)
            .addToBackStack(null) // Thêm vào back stack để quay lại HomeFragment
            .commit()
    }

}